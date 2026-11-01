package com.example.ecommerce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Role;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DataInitializerService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInitializerService.class);


    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializerService(CategoryRepository categoryRepository, ProductRepository productRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            log.info("Initializing database with seed data...");
            seedCategories();
            seedProducts();
            seedUsers();
            log.info("Database initialization complete!");
        } else {
            log.info("Database already initialized. Skipping seed data.");
        }
    }

    private void seedUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@abmart.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("User")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Created admin user: admin/admin123");
        }

        if (!userRepository.existsByUsername("user1")) {
            User user = User.builder()
                    .username("user1")
                    .email("user1@abmart.com")
                    .password(passwordEncoder.encode("user123"))
                    .firstName("Test")
                    .lastName("User")
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            log.info("Created test user: user1/user123");
        }
    }

    private void seedCategories() {
        List<Category> categories = List.of(
                Category.builder()
                        .name("Anime Figures & Statues")
                        .description("Premium collectible anime figures, statues, and action figures from popular anime series.")
                        .imageUrl("/images/categories/figures.jpg")
                        .active(true)
                        .build(),
                Category.builder()
                        .name("Anime Apparel & Clothing")
                        .description("Stylish anime-themed hoodies, t-shirts, jackets, and more for everyday wear.")
                        .imageUrl("/images/categories/apparel.jpg")
                        .active(true)
                        .build(),
                Category.builder()
                        .name("Anime Accessories & Jewelry")
                        .description("Anime-inspired accessories, jewelry, headbands, and cosplay items.")
                        .imageUrl("/images/categories/accessories.jpg")
                        .active(true)
                        .build(),
                Category.builder()
                        .name("Anime Home & Decor")
                        .description("Transform your space with anime-themed lamps, wall art, and home decorations.")
                        .imageUrl("/images/categories/home-decor.jpg")
                        .active(true)
                        .build(),
                Category.builder()
                        .name("Anime Bags & Cases")
                        .description("Functional and stylish anime-themed backpacks, bags, and cases.")
                        .imageUrl("/images/categories/bags.jpg")
                        .active(true)
                        .build(),
                Category.builder()
                        .name("Anime Stationery & Art")
                        .description("Creative anime stationery, art prints, stickers, and collectible notebooks.")
                        .imageUrl("/images/categories/stationery.jpg")
                        .active(true)
                        .build()
        );
        categoryRepository.saveAll(categories);
        log.info("Seeded {} categories", categories.size());
    }

    private void seedProducts() {
        Category figures = categoryRepository.findByNameIgnoreCase("Anime Figures & Statues").orElseThrow();
        Category apparel = categoryRepository.findByNameIgnoreCase("Anime Apparel & Clothing").orElseThrow();
        Category accessories = categoryRepository.findByNameIgnoreCase("Anime Accessories & Jewelry").orElseThrow();
        Category homeDecor = categoryRepository.findByNameIgnoreCase("Anime Home & Decor").orElseThrow();
        Category bags = categoryRepository.findByNameIgnoreCase("Anime Bags & Cases").orElseThrow();
        Category stationery = categoryRepository.findByNameIgnoreCase("Anime Stationery & Art").orElseThrow();

        List<Product> products = List.of(
                // Anime Figures & Statues
                Product.builder()
                        .name("Naruto Uzumaki Sage Mode Figure")
                        .description("Premium 8-inch Naruto figure in Sage Mode pose with detailed chakra effects base. Perfect for any Naruto fan's collection.")
                        .price(new BigDecimal("49.99"))
                        .imageUrl("/images/products/figures/1.jpg")
                        .stockQuantity(50)
                        .category(figures)
                        .active(true)
                        .featured(true)
                        .build(),
                Product.builder()
                        .name("Goku Ultra Instinct Statue")
                        .description("12-inch Dragon Ball Super Goku Ultra Instinct statue with LED light-up aura effect and dynamic action pose.")
                        .price(new BigDecimal("89.99"))
                        .imageUrl("/images/products/figures/2.jpg")
                        .stockQuantity(30)
                        .category(figures)
                        .active(true)
                        .featured(true)
                        .build(),
                Product.builder()
                        .name("Demon Slayer Tanjiro Figure")
                        .description("7-inch Tanjiro Kamado figure with Water Breathing First Form effect parts and interchangeable faces.")
                        .price(new BigDecimal("39.99"))
                        .imageUrl("/images/products/figures/3.jpg")
                        .stockQuantity(60)
                        .category(figures)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("Attack on Titan Levi Ackerman Figure")
                        .description("Premium 9-inch Levi figure with ODM gear, dual blades, and Survey Corps base stand.")
                        .price(new BigDecimal("59.99"))
                        .imageUrl("/images/products/figures/4.jpg")
                        .stockQuantity(40)
                        .category(figures)
                        .active(true)
                        .featured(false)
                        .build(),

                // Anime Apparel & Clothing
                Product.builder()
                        .name("Akatsuki Cloud Hoodie")
                        .description("Premium black hoodie featuring the iconic red Akatsuki cloud pattern. Soft fleece interior, available in S-XXL.")
                        .price(new BigDecimal("44.99"))
                        .imageUrl("/images/products/apparel/1.jpg")
                        .stockQuantity(100)
                        .category(apparel)
                        .active(true)
                        .featured(true)
                        .build(),
                Product.builder()
                        .name("One Piece Straw Hat Crew T-Shirt")
                        .description("Cotton crew neck t-shirt with vibrant Straw Hat Pirates crew illustration. Pre-shrunk cotton.")
                        .price(new BigDecimal("24.99"))
                        .imageUrl("/images/products/apparel/2.jpg")
                        .stockQuantity(150)
                        .category(apparel)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("My Hero Academia UA Jacket")
                        .description("Replica UA High School training jacket with embroidered logo. Lightweight polyester blend.")
                        .price(new BigDecimal("54.99"))
                        .imageUrl("/images/products/apparel/3.jpg")
                        .stockQuantity(80)
                        .category(apparel)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("Jujutsu Kaisen Sukuna T-Shirt")
                        .description("Black t-shirt with Ryomen Sukuna mouth design. 100% cotton, screen printed.")
                        .price(new BigDecimal("22.99"))
                        .imageUrl("/images/products/apparel/4.jpg")
                        .stockQuantity(120)
                        .category(apparel)
                        .active(true)
                        .featured(false)
                        .build(),

                // Anime Accessories & Jewelry
                Product.builder()
                        .name("Naruto Leaf Village Headband")
                        .description("Adjustable metal plate headband with engraved Konoha leaf symbol. Blue fabric band.")
                        .price(new BigDecimal("14.99"))
                        .imageUrl("/images/products/accessories/1.jpg")
                        .stockQuantity(200)
                        .category(accessories)
                        .active(true)
                        .featured(true)
                        .build(),
                Product.builder()
                        .name("Attack on Titan Wings of Freedom Necklace")
                        .description("Stainless steel Survey Corps Wings of Freedom pendant on 24-inch chain.")
                        .price(new BigDecimal("19.99"))
                        .imageUrl("/images/products/accessories/2.jpg")
                        .stockQuantity(180)
                        .category(accessories)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("Dragon Ball Z Potara Earrings Set")
                        .description("Pair of Potara fusion earrings in metallic gold finish with clip-on backs.")
                        .price(new BigDecimal("12.99"))
                        .imageUrl("/images/products/accessories/3.jpg")
                        .stockQuantity(160)
                        .category(accessories)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("One Piece Ace's Hat Orange Beaded Necklace")
                        .description("Replica of Portgas D. Ace's iconic red beaded necklace with smiley pendant.")
                        .price(new BigDecimal("16.99"))
                        .imageUrl("/images/products/accessories/4.jpg")
                        .stockQuantity(140)
                        .category(accessories)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("Demon Slayer Tanjiro Hanafuda Earrings")
                        .description("Pair of Tanjiro's iconic hanafuda flower card earrings with hook backs.")
                        .price(new BigDecimal("11.99"))
                        .imageUrl("/images/products/accessories/5.jpg")
                        .stockQuantity(190)
                        .category(accessories)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("Tokyo Ghoul Kaneki Ken Mask Ring")
                        .description("Stainless steel ring featuring Ken Kaneki's ghoul mask design.")
                        .price(new BigDecimal("9.99"))
                        .imageUrl("/images/products/accessories/6.jpg")
                        .stockQuantity(170)
                        .category(accessories)
                        .active(true)
                        .featured(false)
                        .build(),

                // Anime Home & Decor
                Product.builder()
                        .name("Anime LED Neon Naruto Sign")
                        .description("USB-powered LED neon sign featuring Naruto's face silhouette. 12x10 inches, warm orange glow.")
                        .price(new BigDecimal("34.99"))
                        .imageUrl("/images/products/home-decor/1.jpg")
                        .stockQuantity(70)
                        .category(homeDecor)
                        .active(true)
                        .featured(true)
                        .build(),
                Product.builder()
                        .name("Studio Ghibli Totoro Desk Lamp")
                        .description("Adorable Totoro-shaped desk lamp with adjustable LED brightness. 8 inches tall.")
                        .price(new BigDecimal("29.99"))
                        .imageUrl("/images/products/home-decor/2.jpg")
                        .stockQuantity(90)
                        .category(homeDecor)
                        .active(true)
                        .featured(true)
                        .build(),
                Product.builder()
                        .name("Dragon Ball Z Shenron Incense Holder")
                        .description("Detailed Shenron dragon figure that holds incense sticks along its coiled body.")
                        .price(new BigDecimal("24.99"))
                        .imageUrl("/images/products/home-decor/3.jpg")
                        .stockQuantity(85)
                        .category(homeDecor)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("Attack on Titan 3D Wall Art Panel")
                        .description("3D layered wooden wall art panel featuring the Colossal Titan peering over Wall Maria. 16x12 inches.")
                        .price(new BigDecimal("39.99"))
                        .imageUrl("/images/products/home-decor/4.jpg")
                        .stockQuantity(45)
                        .category(homeDecor)
                        .active(true)
                        .featured(false)
                        .build(),

                // Anime Bags & Cases
                Product.builder()
                        .name("My Hero Academia Deku Backpack")
                        .description("Durable school backpack with Deku costume design, padded laptop compartment, and multiple pockets.")
                        .price(new BigDecimal("36.99"))
                        .imageUrl("/images/products/bags/1.jpg")
                        .stockQuantity(75)
                        .category(bags)
                        .active(true)
                        .featured(true)
                        .build(),
                Product.builder()
                        .name("Sailor Moon Luna Cat Crossbody Bag")
                        .description("Adorable Luna-shaped crossbody bag with crescent moon detail. Faux leather with gold chain strap.")
                        .price(new BigDecimal("28.99"))
                        .imageUrl("/images/products/bags/2.jpg")
                        .stockQuantity(65)
                        .category(bags)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("Naruto Akatsuki Cloud Pencil Case")
                        .description("Black canvas pencil case with red Akatsuki cloud print. Dual zipper compartments.")
                        .price(new BigDecimal("8.99"))
                        .imageUrl("/images/products/bags/3.jpg")
                        .stockQuantity(200)
                        .category(bags)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("One Piece Treasure Chest Messenger Bag")
                        .description("Treasure chest-styled messenger bag with Straw Hat Jolly Roger emblem. Canvas with leather accents.")
                        .price(new BigDecimal("32.99"))
                        .imageUrl("/images/products/bags/4.jpg")
                        .stockQuantity(55)
                        .category(bags)
                        .active(true)
                        .featured(false)
                        .build(),

                // Anime Stationery & Art
                Product.builder()
                        .name("Anime Girl Holographic Sticker Pack (50pc)")
                        .description("50 assorted holographic anime girl stickers featuring popular characters. Waterproof vinyl.")
                        .price(new BigDecimal("7.99"))
                        .imageUrl("/images/products/stationery/1.jpg")
                        .stockQuantity(300)
                        .category(stationery)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("Studio Ghibli Watercolor Art Print Set")
                        .description("Set of 4 watercolor-style art prints featuring iconic Ghibli scenes. 8x10 inches each on premium cardstock.")
                        .price(new BigDecimal("18.99"))
                        .imageUrl("/images/products/stationery/2.jpg")
                        .stockQuantity(110)
                        .category(stationery)
                        .active(true)
                        .featured(true)
                        .build(),
                Product.builder()
                        .name("Death Note Replica Notebook")
                        .description("High-quality replica of the Death Note with 'How to Use' rules page. Feather quill pen included.")
                        .price(new BigDecimal("15.99"))
                        .imageUrl("/images/products/stationery/3.jpg")
                        .stockQuantity(130)
                        .category(stationery)
                        .active(true)
                        .featured(false)
                        .build(),
                Product.builder()
                        .name("Jujutsu Kaisen Washi Tape Set (6 rolls)")
                        .description("Six rolls of decorative washi tape featuring Jujutsu Kaisen characters and cursed energy patterns.")
                        .price(new BigDecimal("10.99"))
                        .imageUrl("/images/products/stationery/4.jpg")
                        .stockQuantity(160)
                        .category(stationery)
                        .active(true)
                        .featured(false)
                        .build()
        );

        productRepository.saveAll(products);
        log.info("Seeded {} products", products.size());
    }
}
