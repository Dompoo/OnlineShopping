package com.dompoo.onlineshopping.repository.postRepository;

import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
