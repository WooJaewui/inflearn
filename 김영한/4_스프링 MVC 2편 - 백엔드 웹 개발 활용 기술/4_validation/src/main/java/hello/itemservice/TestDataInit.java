package hello.itemservice;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() throws SQLException {

        ExecutorService exe = Executors.newFixedThreadPool(10);
        ExecutorService exe2 = Executors.newCachedThreadPool();
        ExecutorService exe3 = Executors.newScheduledThreadPool(10);
        ExecutorService exe4 = Executors.newSingleThreadExecutor();

        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}