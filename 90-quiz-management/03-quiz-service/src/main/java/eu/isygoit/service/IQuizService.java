package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.model.Quiz;
import eu.isygoit.model.QuizQuestion;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * The interface Quiz service.
 */
public interface IQuizService extends ICrudServiceMethod<Long, Quiz> {

    /**
     * Download question image resource.
     *
     * @param id the id
     * @return the resource
     * @throws MalformedURLException the malformed url exception
     */
    Resource downloadQuestionImage(Long id) throws MalformedURLException;

    /**
     * Upload question image quiz question.
     *
     * @param id     the id
     * @param domain the domain
     * @param file   the file
     * @return the quiz question
     * @throws IOException the io exception
     */
    QuizQuestion uploadQuestionImage(Long id, String domain, MultipartFile file) throws IOException;

    /**
     * Gets quiz codes by category.
     *
     * @param category the category
     * @return the quiz codes by category
     */
    List<Quiz> getQuizCodesByCategory(String category);

    /**
     * Gets quiz by code.
     *
     * @param code the code
     * @return the quiz by code
     */
    Quiz getQuizByCode(String code);
}
