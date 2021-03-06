package com.kakao.cafe.article.infra;

import com.kakao.cafe.article.domain.Article;
import com.kakao.cafe.article.domain.ArticleRepository;
import com.kakao.cafe.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleJdbcRepository implements ArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ArticleJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Article article) {
        String query = "insert into articles(author_id, title, content, created_at) values(?, ?, ?, ?)";

        return jdbcTemplate.update(
                query,
                article.getAuthorId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt()
        );
    }

    @Override
    public List<Article> findAll() {
        String query = "select * from articles A join users B on A.author_id = B.user_id";
        return jdbcTemplate.query(query, mapArticleRow());
    }

    @Override
    public Article findByIdOrNull(int articleId) {
        String query = "select * from articles A join users B on A.author_id = B.user_id where id = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapArticleRow(), articleId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Article article) {
        int articleId = article.getId();
        String query = "update articles set title = ?, content = ? where id = ?";
        jdbcTemplate.update(query, article.getTitle(), article.getContent(), articleId);
    }

    @Override
    public void delete(Article article) {
        int articleId = article.getId();
        String query = "delete from articles where id = ?";
        jdbcTemplate.update(query, articleId);
    }

    private RowMapper<Article> mapArticleRow() {
        return (rs, rowNum) ->
                Article.valueOf(
                        rs.getInt("id"),
                        rs.getString("author_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("created_at"),
                        User.valueOf(
                                rs.getString("user_id"),
                                rs.getString("password"),
                                rs.getString("username"),
                                rs.getString("email")
                        )
                );
    }
}
