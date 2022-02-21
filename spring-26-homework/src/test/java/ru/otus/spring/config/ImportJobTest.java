package ru.otus.spring.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.models.*;
import ru.otus.spring.repositories.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.config.JobConfig.IMPORT_JOB_NAME;

@SpringBootTest
@SpringBatchTest
class ImportJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorMongoRepository authorMongoRepository;

    @Autowired
    private GenreMongoRepository genreMongoRepository;

    @Autowired
    private BookMongoRepository bookMongoRepository;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJob() throws Exception {
        List<AuthorJPA> expectedAuthors = authorRepository.findAll();
        List<GenreJPA> expectedGenres = genreRepository.findAll();
        List<BookJPA> expectedBooks = bookRepository.findAll();

        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_JOB_NAME);

        JobParameters parameters = new JobParametersBuilder()
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        List<AuthorMongo> actualAuthors = authorMongoRepository.findAll();
        List<GenreMongo> actualGenres = genreMongoRepository.findAll();
        List<BookMongo> actualBooks = bookMongoRepository.findAll();

        for(AuthorMongo actualAuthor : actualAuthors) {
            for(AuthorJPA expectedAuthor : expectedAuthors) {
                if (Long.valueOf(actualAuthor.getJPADbId()).equals(expectedAuthor.getId())) {
                    assertThat(actualAuthor.getFirstName()).isEqualTo(expectedAuthor.getFirstName());
                    assertThat(actualAuthor.getLastName()).isEqualTo(expectedAuthor.getLastName());
                }
            }
        }

        for(GenreMongo actualGenre : actualGenres) {
            for(GenreJPA expectedGenre : expectedGenres) {
                if (Long.valueOf(actualGenre.getJPADbId()).equals(expectedGenre.getId())) {
                    assertThat(actualGenre.getName()).isEqualTo(expectedGenre.getName());
                }
            }
        }

        for(BookMongo actualBook : actualBooks) {
            for(BookJPA expectedBook : expectedBooks) {
                if (Long.valueOf(actualBook.getJPADbId()).equals(expectedBook.getId())) {
                    assertThat(actualBook.getName()).isEqualTo(expectedBook.getName());
                    assertThat(Long.valueOf(actualBook.getAuthors().get(0).getJPADbId())).isEqualTo(expectedBook.getAuthor().getId());
                    assertThat(actualBook.getAuthors().get(0).getFirstName()).isEqualTo(expectedBook.getAuthor().getFirstName());
                    assertThat(actualBook.getAuthors().get(0).getLastName()).isEqualTo(expectedBook.getAuthor().getLastName());
                    assertThat(Long.valueOf(actualBook.getGenres().get(0).getJPADbId())).isEqualTo(expectedBook.getGenre().getId());
                    assertThat(actualBook.getGenres().get(0).getName()).isEqualTo(expectedBook.getGenre().getName());
                }
            }
        }

    }
}