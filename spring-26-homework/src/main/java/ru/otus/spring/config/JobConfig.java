package ru.otus.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import ru.otus.spring.models.*;
import ru.otus.spring.repositories.*;
import ru.otus.spring.service.ObjectTransformService;

import java.util.List;
import java.util.Map;

@Configuration
public class JobConfig {
    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch");

    public static final String IMPORT_JOB_NAME = "importFromJPAtoMongo";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ObjectTransformService objectTransformService;

    @Autowired
    private AuthorMongoRepository authorMongoRepository;

    @Autowired
    private GenreMongoRepository genreMongoRepository;

    @Autowired
    private BookMongoRepository bookMongoRepository;

    @StepScope
    @Bean
    public ItemReader<AuthorJPA> authorReader(AuthorRepository authorRepository) {
        return new RepositoryItemReaderBuilder<AuthorJPA>()
                .name("authorItemReader")
                .repository(authorRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<AuthorJPA, AuthorMongo> authorProcessor() {
        return this.objectTransformService::toAuthorMongo;
    }

    @StepScope
    @Bean
    public ItemWriter<AuthorMongo> authorWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<AuthorMongo>()
                .template(mongoTemplate)
                .collection("author")
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<GenreJPA> genreReader(GenreRepository genreRepository) {
        return new RepositoryItemReaderBuilder<GenreJPA>()
                .name("genreItemReader")
                .repository(genreRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<GenreJPA, GenreMongo> genreProcessor() {
        return this.objectTransformService::toGenreMongo;
    }

    @StepScope
    @Bean
    public ItemWriter<GenreMongo> genreWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<GenreMongo>()
                .template(mongoTemplate)
                .collection("genre")
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<BookJPA> bookReader(BookRepository bookRepository) {
        return new RepositoryItemReaderBuilder<BookJPA>()
                .name("bookItemReader")
                .repository(bookRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<BookJPA, BookMongo> bookProcessor() {
        return this.objectTransformService::toBookMongo;
    }

    @StepScope
    @Bean
    public ItemWriter<BookMongo> bookWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<BookMongo>()
                .template(mongoTemplate)
                .collection("book")
                .build();
    }

    @Bean
    public Job importJob(Step migrateAuthorsStep, Step migrateGenresStep, Step migrateBooksStep) {
        return jobBuilderFactory.get(IMPORT_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow(migrateAuthorsStep, migrateGenresStep))
                //.flow(migrateAuthorsStep)
                //.next(migrateGenresStep)
                .next(migrateBooksStep)
                .build()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                        objectTransformService.init();
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Flow splitFlow(Step firstStep, Step secondStep) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                //.add(firstFlow(firstStep), secondFlow(secondStep))
                .add(
                        new FlowBuilder<SimpleFlow>("firstFlow")
                                .start(firstStep)
                                .build(),
                        new FlowBuilder<SimpleFlow>("secondFlow")
                                .start(secondStep)
                                .build())
                .build();
    }

    @Bean
    public Step migrateAuthorsStep(ItemReader<AuthorJPA> reader, ItemWriter<AuthorMongo> writer,
                                     ItemProcessor<AuthorJPA, AuthorMongo> itemProcessor) {
        return stepBuilderFactory.get("migrateAuthorsStep")
                .<AuthorJPA, AuthorMongo>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull AuthorJPA o) {
                        logger.info("Конец чтения");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<>() {
                    public void beforeWrite(@NonNull List list) {
                        logger.info("Начало записи");
                    }

                    public void afterWrite(@NonNull List list) {
                        objectTransformService.cacheAuthors(authorMongoRepository.findAll());
                        logger.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(AuthorJPA o) {
                        logger.info("Начало обработки");
                    }

                    public void afterProcess(@NonNull AuthorJPA o, AuthorMongo o2) {
                        logger.info("Конец обработки");
                    }

                    public void onProcessError(@NonNull AuthorJPA o, @NonNull Exception e) {
                        logger.info("Ошибка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
                .build();
    }

    @Bean
    public Step migrateGenresStep(ItemReader<GenreJPA> reader, ItemWriter<GenreMongo> writer,
                                   ItemProcessor<GenreJPA, GenreMongo> itemProcessor) {
        return stepBuilderFactory.get("migrateGenresStep")
                .<GenreJPA, GenreMongo>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull GenreJPA o) {
                        logger.info("Конец чтения");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<>() {
                    public void beforeWrite(@NonNull List list) {
                        logger.info("Начало записи");
                    }

                    public void afterWrite(@NonNull List list) {
                        objectTransformService.cacheGenres(genreMongoRepository.findAll());
                        logger.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(GenreJPA o) {
                        logger.info("Начало обработки");
                    }

                    public void afterProcess(@NonNull GenreJPA o, GenreMongo o2) {
                        logger.info("Конец обработки");
                    }

                    public void onProcessError(@NonNull GenreJPA o, @NonNull Exception e) {
                        logger.info("Ошибка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
                .build();
    }

    @Bean
    public Step migrateBooksStep(ItemReader<BookJPA> reader, ItemWriter<BookMongo> writer,
                                   ItemProcessor<BookJPA, BookMongo> itemProcessor) {
        return stepBuilderFactory.get("migrateBooksStep")
                .<BookJPA, BookMongo>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull BookJPA o) {
                        logger.info("Конец чтения");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<>() {
                    public void beforeWrite(@NonNull List list) {
                        logger.info("Начало записи");
                    }

                    public void afterWrite(@NonNull List list) {
                        objectTransformService.cacheBooks(bookMongoRepository.findAll());
                        logger.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(BookJPA o) {
                        logger.info("Начало обработки");
                        objectTransformService.cacheAuthors(authorMongoRepository.findAll());
                        objectTransformService.cacheGenres(genreMongoRepository.findAll());
                    }

                    public void afterProcess(@NonNull BookJPA o, BookMongo o2) {
                        logger.info("Конец обработки");
                    }

                    public void onProcessError(@NonNull BookJPA o, @NonNull Exception e) {
                        logger.info("Ошибка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
