package com.example.PasteleriaBackend.config.seed;

import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.BlogPost;
import com.example.PasteleriaBackend.domain.repository.BlogPostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Order(50)
@Transactional
public class BlogSeeder implements ApplicationRunner {

    private final BlogPostRepository blogPostRepository;
    private final ObjectMapper objectMapper;

    public BlogSeeder(BlogPostRepository blogPostRepository, ObjectMapper objectMapper) {
        this.blogPostRepository = blogPostRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (blogPostRepository.count() > 0) {
            return;
        }

        BlogPost recordCake = new BlogPost();
        recordCake.setSlug("la-torta-gigante-que-batio-record");
        recordCake.setTitle("La torta gigante que batió récord");
        recordCake.setHeroImage("/img/blog1.png");
        recordCake.setHeroCaption("Una dulce hazaña en la plaza central");
        recordCake.setExcerpt("Cómo logramos servir más de 2.000 porciones en una sola celebración");
        recordCake.setBody(writeBody(List.of(
            Map.of("type", "p", "content", "La planificación comenzó meses antes: logística, seguridad y una receta que pudiera escalar a miles de porciones."),
            Map.of("type", "p", "content", "El día del evento participaron estudiantes, vecinos y colaboradores que ayudaron a armar la torta de 2 metros de diámetro."),
            Map.of("type", "p", "content", "Además del récord local, recaudamos fondos para organizaciones sociales de la comuna.")
        )));

        BlogPost veganCookies = new BlogPost();
        veganCookies.setSlug("secretos-de-nuestras-galletas-veganas");
        veganCookies.setTitle("Secretos de nuestras galletas veganas");
        veganCookies.setHeroImage("/img/blog2.png");
        veganCookies.setHeroCaption("Ingredientes 100% de origen vegetal");
        veganCookies.setExcerpt("Textura crujiente, sabor intenso y cero ingredientes de origen animal.");
        veganCookies.setBody(writeBody(List.of(
            Map.of("type", "p", "content", "La receta combina harina de almendras, avena integral y chips de cacao certificados."),
            Map.of("type", "p", "content", "El secreto está en el descanso de la masa: refrigerada por 12 horas para lograr la textura perfecta."),
            Map.of("type", "p", "content", "Hoy es nuestro producto más vendido en ferias saludables y coffee breaks corporativos.")
        )));

        blogPostRepository.saveAll(List.of(recordCake, veganCookies));
    }

    private String writeBody(List<Map<String, Object>> blocks) {
        try {
            return objectMapper.writeValueAsString(blocks);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No se pudo serializar el contenido del blog", ex);
        }
    }
}
