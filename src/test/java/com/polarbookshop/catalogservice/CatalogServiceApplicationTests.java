package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

// 加载完整的应用程序上下文和监听随机端口的servlet容器
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class CatalogServiceApplicationTests {
	@Autowired
	private WebTestClient webTestClient;
	@Test
	void whenPostRequestThenBookCreate(){
		var expectedBook = new Book("1234567890", "Title", "Author", 9.90);
		webTestClient
				.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(expectedBook.isbn());
				});
	}
	void contextLoads() {
	}

}
