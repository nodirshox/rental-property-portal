package com.airbnb.property.repository;
import com.airbnb.property.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private String HASH_KEY = "Product";


    private final RedisTemplate template;


    public Product save(Product product) {
        template.opsForHash().put(HASH_KEY, product.getId(), product);
        template.expire(HASH_KEY, 20, TimeUnit.SECONDS);
        return product;
    }

    public List<Product> findAll() {
        return template.opsForHash().values(HASH_KEY);
    }

    public Product getById(int id) {
        return (Product)template.opsForHash().get(HASH_KEY, id);
    }

    public String deleteProductById(int id) {
        template.opsForHash().delete(HASH_KEY, id);
        return "Product deleted";
    }
}
