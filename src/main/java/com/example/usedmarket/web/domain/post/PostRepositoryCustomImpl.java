package com.example.usedmarket.web.domain.post;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.usedmarket.web.domain.post.QPost.post;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Post> findByPostTitle(String postTitle) {
        return queryFactory.select(post)
                .from(post)
                .where(post.title.like("%" + postTitle + "%"))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findByStatusIsSell() {
        return queryFactory.select(post)
                .from(post)
                .where(post.status.eq(PostStatus.SELL))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findByAllPost() {
        return queryFactory.select(post)
                .from(post)
                .orderBy(post.createdAt.desc())
                .fetch();
    }
}
