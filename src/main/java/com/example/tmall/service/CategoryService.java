package com.example.tmall.service;

import com.example.tmall.dao.CategoryDAO;
import com.example.tmall.pojo.Category;
import com.example.tmall.pojo.Product;
import com.example.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;

    @Cacheable(key = "'categories-all'")
    public List<Category> list(){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    @Cacheable(key = "'categories-page-' + #p0 + '-' + #p1")
    public Page4Navigator<Category> list(int start, int size, int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = categoryDAO.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    @CacheEvict(allEntries = true)
    public void add(Category category){
        categoryDAO.save(category);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        categoryDAO.deleteById(id);
    }

    @Cacheable(key = "'categories-one' + #p0")
    public Category get(int id){
        return categoryDAO.getOne(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Category bean){
        categoryDAO.save(bean);
    }

    public void removeCategoryFromProduct(List<Category> cs){
        for(Category category : cs){
            removeCategoryFromProduct(category);
        }
    }

    public void removeCategoryFromProduct(Category category) {
        List<Product> products = category.getProducts();
        if(null != products){
            for(Product product : products)
                product.setCategory(null);
        }

        List<List<Product>> productsByRow = category.getProductsByRow();
        if(null != productsByRow){
            for(List<Product> ps : productsByRow){
                for(Product p : ps)
                    p.setCategory(null);
            }
        }
    }
}
