package com.dompoo.onlineshopping.repository.postRepository;

import com.dompoo.onlineshopping.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
