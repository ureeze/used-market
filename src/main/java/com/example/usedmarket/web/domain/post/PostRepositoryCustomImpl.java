package com.example.usedmarket.web.domain.post;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.example.usedmarket.web.domain.book.QBook.book;
import static com.example.usedmarket.web.domain.post.QPost.post;
import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Post> findByPostTitle(String postTitle) {
        return queryFactory.select(post)
                .from(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.title.like("%" + postTitle + "%"))
                .where(book.deleted.eq(false))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findByStatusIsSell() {
        return queryFactory.select(post)
                .from(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.status.eq(PostStatus.SELL))
                .where(post.deleted.eq(false))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findByPostIsNotDeleted() {
        return queryFactory.select(post)
                .from(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.deleted.eq(false))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findByAllPostAboutMyself(Long userId) {
        return queryFactory.select(post)
                .from(post)
                .leftJoin(post.bookList, book)
                .fetchJoin()
                .where(post.userEntity.id.eq(userId))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public Optional<Post> findByPostId(Long postId) {
        return Optional.ofNullable(queryFactory.select(post)
                .from(post)
                .leftJoin(post.userEntity, userEntity)
                .fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne());
    }
}