package com.buszta.cryptotracker;

import com.buszta.cryptotracker.portfolio.historical.CoinHistoricalData;
import com.buszta.cryptotracker.portfolio.historical.CoinHistoricalDataRepository;
import com.buszta.cryptotracker.portfolio.historical.HistoricalData;
import com.buszta.cryptotracker.portfolio.historical.HistoricalDataRepository;
import com.buszta.cryptotracker.portfolio.transaction.Transaction;
import com.buszta.cryptotracker.portfolio.transaction.TransactionRepository;
import com.buszta.cryptotracker.user.User;
import com.buszta.cryptotracker.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
public class CryptoTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CryptoTrackerApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(UserRepository userRepository, HistoricalDataRepository historicalDataRepository, PasswordEncoder passwordEncoder){
//        return args -> {
//            User user = new User(1, "buszta-jakub@wp.pl", passwordEncoder.encode("123"));
//            userRepository.save(user);
//            Random random = new Random();
//
//            for (int i = 1000; i >= 0; i--){
//                HistoricalData historicalData = new HistoricalData();
//                historicalData.setUser(user);
//                historicalData.setDataValue((double) (random.nextInt(10000 - 1000 + 1) + 1000));
//                historicalData.setProfitLoss((double) random.nextInt(10000 - 1000 + 1) + 1000);
//                historicalData.setPlacedAt(LocalDate.from(LocalDateTime.now().minusDays(i)));
//
//                historicalDataRepository.save(historicalData);
//            }
//
//        };
//    }


//    @Bean
//    CommandLineRunner commandLineRunner(UserRepository userRepository, HistoricalDataRepository historicalDataRepository, TransactionRepository transactionRepository, CoinHistoricalDataRepository coinHistoricalDataRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            User user = new User(1, "buszta-jakub@wp.pl", passwordEncoder.encode("123"));
//            userRepository.save(user);
//            double [] sumValue = {61733, 64106, 63967, 64446, 66387, 67126, 66897};
//
//            Transaction transaction = new Transaction();
//            transaction.setUser(user);
//            transaction.setQuantity(0.5);
//            transaction.setPrice(16000.0);
//            transaction.setCoinId("bitcoin");
//            transaction.setPlacedAt(LocalDate.from(LocalDate.now().minusDays(sumValue.length)));
//            transactionRepository.save(transaction);
//
//            Transaction transaction2 = new Transaction();
//            transaction2.setUser(user);
//            transaction2.setQuantity(11.3);
//            transaction2.setPrice(1681.0);
//            transaction2.setCoinId("ethereum");
//            transaction2.setPlacedAt(LocalDate.from(LocalDate.now().minusDays(sumValue.length)));
//            transactionRepository.save(transaction2);
//
//            Transaction transaction3 = new Transaction();
//            transaction3.setUser(user);
//            transaction3.setQuantity(24360.0);
//            transaction3.setPrice(0.574);
//            transaction3.setCoinId("ripple");
//            transaction3.setPlacedAt(LocalDate.from(LocalDate.now().minusDays(sumValue.length)));
//            transactionRepository.save(transaction3);
//
//            Transaction transaction4 = new Transaction();
//            transaction4.setUser(user);
//            transaction4.setQuantity(35.0);
//            transaction4.setPrice(200.0);
//            transaction4.setCoinId("binancecoin");
//            transaction4.setPlacedAt(LocalDate.from(LocalDate.now().minusDays(sumValue.length)));
//            transactionRepository.save(transaction4);
//
//            double [] sumProfit = {5733, 8106, 7967, 8446, 10387, 11119, 10897};
//
//            double [] sumProfitBtc = {1620, 1500, 1330, 1700, 2900, 2340, 2500};
//            double [] sumProfitEth = {1837, 2357, 2198, 2266, 3837, 5069, 4221};
//            double [] sumProfitBnb = {1295, 1855, 1680, 1575, 1890, 2023, 1855};
//            double [] sumProfitXrp = {981, 2394, 2759, 2905, 1760, 1687, 2321};
//
//            double [] sumValueBtc = {17620, 17500, 17330, 17700, 18900, 18340, 18500};
//            double [] sumValueEth = {20837, 21357, 21198, 21266, 22837, 24069, 23221};
//            double [] sumValueBnb = {8295, 8855, 8680, 8575, 8890, 9030, 8855};
//            double [] sumValueXrp = {14981, 16394, 16759, 16905, 15760, 15687, 16321};
//
//            for (int i = 0; i < sumValue.length; i++){
//               HistoricalData historicalData = new HistoricalData();
//               historicalData.setUser(user);
//               historicalData.setDataValue(sumValue[i]);
//               historicalData.setProfitLoss(sumProfit[i]);
//               historicalData.setPlacedAt(LocalDate.from(LocalDate.now().minusDays(sumValue.length-i)));
//
//               historicalDataRepository.save(historicalData);
//            }
//
//            for (int i = 0; i < sumValue.length; i++){
//                CoinHistoricalData coinHistoricalData = new CoinHistoricalData();
//
//                coinHistoricalData.setUser(user);
//                coinHistoricalData.setCoinId("bitcoin");
//                coinHistoricalData.setProfitLoss(sumProfitBtc[i]);
//                coinHistoricalData.setDataValue(sumValueBtc[i]);
//                coinHistoricalData.setPlacedAt(LocalDate.from(LocalDate.now().minusDays(sumValue.length-i)));
//
//                coinHistoricalDataRepository.save(coinHistoricalData);
//            }
//
//            for (int i = 0; i < sumValue.length; i++){
//                CoinHistoricalData coinHistoricalData = new CoinHistoricalData();
//
//                coinHistoricalData.setUser(user);
//                coinHistoricalData.setCoinId("ethereum");
//                coinHistoricalData.setProfitLoss(sumProfitEth[i]);
//                coinHistoricalData.setDataValue(sumValueEth[i]);
//
//                int daysToSubtract = sumValueBtc.length - i;
//                coinHistoricalData.setPlacedAt(LocalDate.from(LocalDate.now().minusDays(daysToSubtract)));
//
//                coinHistoricalDataRepository.save(coinHistoricalData);
//            }
//
//            for (int i = 0; i < sumValue.length; i++){
//                CoinHistoricalData coinHistoricalData = new CoinHistoricalData();
//
//                coinHistoricalData.setUser(user);
//                coinHistoricalData.setCoinId("binancecoin");
//                coinHistoricalData.setProfitLoss(sumProfitBnb[i]);
//                coinHistoricalData.setDataValue(sumValueBnb[i]);
//                coinHistoricalData.setPlacedAt(LocalDate.from(LocalDate.now().minusDays(sumValue.length-i)));
//
//                coinHistoricalDataRepository.save(coinHistoricalData);
//            }
//
//            for (int i = 0; i < sumValue.length; i++){
//                CoinHistoricalData coinHistoricalData = new CoinHistoricalData();
//
//                coinHistoricalData.setUser(user);
//                coinHistoricalData.setCoinId("ripple");
//                coinHistoricalData.setProfitLoss(sumProfitXrp[i]);
//                coinHistoricalData.setDataValue(sumValueXrp[i]);
//                coinHistoricalData.setPlacedAt(LocalDate.from(LocalDate.now().minusDays(sumValue.length-i)));
//
//                coinHistoricalDataRepository.save(coinHistoricalData);
//            }
//        };
//    }

}
