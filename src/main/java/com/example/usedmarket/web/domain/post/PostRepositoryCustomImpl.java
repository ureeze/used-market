package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.dto.PostResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.usedmarket.web.domain.book.QBook.book;
import static com.example.usedmarket.web.domain.post.QPost.post;
import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // POST TITLE 로 PAGING 된 POST 조회
    @Override
    public Page<PostResponseDto> findByPostTitle(Long userId, String postTitle, Pageable pageable) {
        log.info("FIND POST BY POST TITLE");
        List<PostResponseDto> content = queryFactory.selectFrom(post)
                .join(post.bookList, book)
                .where(post.title.like("%" + postTitle + "%"), book.deleted.eq(false))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().map(post1 -> PostResponseDto.toResponseDto(userId, post1))
                .collect(Collectors.toList());

        // SIZE 계산
        log.info("TOTAL SIZE QUERY");
        List<Post> postList = queryFactory.selectFrom(post)
                .join(post.bookList, book)
                .where(post.title.like("%" + postTitle + "%"), book.deleted.eq(false))
                .fetch();

        return new PageImpl<>(content, pageable, postList.size());
    }

    // POST 상태가 SELL 인 POST LIST 조회
    @Override
    public List<Post> findByStatusIsSell() {
        log.info("FIND POST BY POST STATUS IS SELL");
        return queryFactory.selectFrom(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.status.eq(PostStatus.SELL), post.deleted.eq(false))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    // 삭제되지 않은 PAGING 된 POST 조회
    @Override
    public Page<PostResponseDto> findByNotDeletedPost(Long userId, Pageable pageable) {
        log.info("FIND POST BY NOT DELETED POST");
        List<PostResponseDto> content = queryFactory.selectFrom(post)
                .join(post.bookList, book)
                .where(post.deleted.eq(false))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().map(post1 -> PostResponseDto.toResponseDto(userId, post1))
                .collect(Collectors.toList());

        // SIZE 계산
        log.info("TOTAL SIZE QUERY");
        List<Post> postList = queryFactory.selectFrom(post)
                .join(post.bookList, book)
                .where(post.deleted.eq(false))
                .fetch();
        return new PageImpl<>(content, pageable, postList.size());
    }

    // 현재 사용자의 POST LIST 조회
    @Override
    public List<Post> findByCurrentUser(Long userId) {
        log.info("FIND POST BY CURRENT USER");
        return queryFactory.selectFrom(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.userEntity.id.eq(userId))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    // POST ID 로 POST 조회
    @Override
    public Optional<Post> findByPostId(Long postId) {
        log.info("FIND POST BY POST ID");
        return Optional.ofNullable(queryFactory.selectFrom(post)
                .leftJoin(post.userEntity, userEntity)
                .fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne());
    }
}