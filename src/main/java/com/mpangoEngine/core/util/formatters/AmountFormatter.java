package com.mpangoEngine.core.util.formatters;

import org.springframework.format.Formatter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mpangoEngine.core.model.TransactionAmount;

public class AmountFormatter implements Formatter<TransactionAmount> {

	private static final Pattern amountPattern = Pattern.compile("(\\d+)(\\w+)");

	@Override
	public TransactionAmount parse(String text, Locale locale) throws ParseException {
		Matcher matcher = amountPattern.matcher(text);
		if (matcher.find()) {
			try {
				TransactionAmount ta = new TransactionAmount();
				ta.setAmount(new BigDecimal(Integer.parseInt(matcher.group(1))));
				ta.setCurrency(Currency.getInstance(matcher.group(2)));
				return ta;
			} catch (Exception e) {
			}
		}
		return new TransactionAmount();
	}

	@Override
	public String print(TransactionAmount tradeAmount, Locale locale) {
		return tradeAmount.getAmount().toPlainString() + tradeAmount.getCurrency().getSymbol(locale);
	}

}
