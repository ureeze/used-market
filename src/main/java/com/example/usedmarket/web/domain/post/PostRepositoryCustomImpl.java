package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.dto.PostResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.usedmarket.web.domain.book.QBook.book;
import static com.example.usedmarket.web.domain.post.QPost.post;
import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostResponseDto> findByPostTitle(Long userId, String postTitle, Pageable pageable) {
        List<PostResponseDto> content = queryFactory.selectFrom(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.title.like("%" + postTitle + "%"), book.deleted.eq(false))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().map(post1 -> PostResponseDto.toResponseDto(userId, post1))
                .collect(Collectors.toList());
        List<Post> postList = queryFactory.selectFrom(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.title.like("%" + postTitle + "%"), book.deleted.eq(false))
                .fetch();
        return new PageImpl<>(content, pageable, postList.size());

    }

    @Override
    public List<Post> findByStatusIsSell() {
        return queryFactory.selectFrom(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.status.eq(PostStatus.SELL), post.deleted.eq(false))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public Page<PostResponseDto> findByPostIsNotDeleted(Long userId, Pageable pageable) {
        List<PostResponseDto> content = queryFactory.selectFrom(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.deleted.eq(false))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().map(post1 -> PostResponseDto.toResponseDto(userId, post1))
                .collect(Collectors.toList());
        List<Post> postList = queryFactory.selectFrom(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.deleted.eq(false))
                .fetch();
        return new PageImpl<>(content, pageable, postList.size());
    }

    @Override
    public List<Post> findByAllPostAboutMyself(Long userId) {
        return queryFactory.selectFrom(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.userEntity.id.eq(userId))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public Optional<Post> findByPostId(Long postId) {
        return Optional.ofNullable(queryFactory.selectFrom(post)
                .leftJoin(post.userEntity, userEntity)
                .fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne());
    }
}