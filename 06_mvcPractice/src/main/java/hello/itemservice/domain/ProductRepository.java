package hello.itemservice.domain;

import java.util.HashMap;
import java.util.Map;

public class ProductRepository {

    private Map<Long, Product> repository = new HashMap<>();

    public Map<Long, Product> products() {
        return repository;
    }

    public Product getProductById(Long id) {
        return repository.get(id);
    }

    public void save(Product product) {
        repository.put(product.getId(), product);
    }
}
