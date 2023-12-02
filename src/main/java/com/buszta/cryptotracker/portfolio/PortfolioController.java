package com.buszta.cryptotracker.portfolio;

import com.buszta.cryptotracker.common.CommonController;
import com.buszta.cryptotracker.portfolio.historical.CoinHistoricalDataService;
import com.buszta.cryptotracker.portfolio.historical.HistoricalDataRepository;
import com.buszta.cryptotracker.ApiCallScheduler;
import com.buszta.cryptotracker.portfolio.transaction.Transaction;
import com.buszta.cryptotracker.portfolio.transaction.TransactionService;
import com.buszta.cryptotracker.security.SecurityUtilis;
import com.buszta.cryptotracker.user.User;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class PortfolioController extends CommonController {
    private final ApiCallScheduler apiCallScheduler;
    private final TransactionService transactionService;
    private final HistoricalDataRepository historicalDataRepository;
    private final CoinHistoricalDataService coinHistoricalDataService;

    public PortfolioController(ApiCallScheduler apiCallScheduler, ApiCallScheduler apiCallScheduler1, TransactionService transactionService, HistoricalDataRepository historicalDataRepository, CoinHistoricalDataService coinHistoricalDataService) {
        super(apiCallScheduler);
        this.apiCallScheduler = apiCallScheduler1;
        this.transactionService = transactionService;
        this.historicalDataRepository = historicalDataRepository;
        this.coinHistoricalDataService = coinHistoricalDataService;
    }

    @GetMapping("/portfolio")
    private String viewPortfolioPage(Model model) {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();
        model.addAttribute("listOfCrypto", apiCallScheduler.getCoinMarketsUSD());

        List<Coin> listOfCoins = transactionService.getListOfCoinsByCompressedTransaction(requestUser);
        if (listOfCoins.isEmpty()) {
            return "portfolio/portfolio_empty";
        }

        transactionService.calculateProfitLoss(listOfCoins, model, requestUser);
        coinHistoricalDataService.saveCoinHistoricalData(listOfCoins, requestUser);

        model.addAttribute("historicalData", historicalDataRepository.findByUser(requestUser));
        return "portfolio/portfolio";
    }

    @PostMapping("/portfolio/transaction/add")
    private String addTransaction(@ModelAttribute CoinMarkets coin, Model model) {
        BigDecimal currentPriceOfClickedCrypto = apiCallScheduler.getPrice(coin.getId());
        model.addAttribute("crypto", coin);
        model.addAttribute("currentPriceOfClickedCrypto", currentPriceOfClickedCrypto);
        return "transaction";
    }

    @PostMapping("/portfolio/transaction")
    private String submitTransaction(@ModelAttribute Transaction transaction) {
        transactionService.save(transaction);
        return "redirect:/portfolio";
    }

    @PostMapping("/portfolio/transaction/delete")
    private String deleteTransaction(@ModelAttribute("deleteCryptoId") String cryptoId) {
        transactionService.deleteCoinFromPortfolio(cryptoId);
        return "redirect:/portfolio";
    }

}
