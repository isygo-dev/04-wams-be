package eu.isygoit.service.impl;


import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.CrudService;
import eu.isygoit.model.PostComment;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.PostCommentRepository;
import eu.isygoit.service.IPostCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Post comment service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = PostCommentRepository.class)
public class PostCommentService extends CrudService<Long, PostComment, PostCommentRepository> implements IPostCommentService {

}
