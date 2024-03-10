package hello.itemservice.domain;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRepositoryTest {

    ProductRepository repository = new ProductRepository();

    @Test
    void save() {
        //given
        Product product = new Product(
                1L,
                "productA",
                2000,
                3
        );

        //when
        repository.save(product);

        //then
        Product productA = repository.getProductById(product.getId());
        assertThat(productA).isEqualTo(product);
    }
}
