package com.sl.app.component.template;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sl.util.Util;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class YearMonthInputFields extends Div {

	private static final long serialVersionUID = 9179383500516238211L;
	private final ComboBox<Integer> yearPicker;
    private final ComboBox<Month> monthPicker;

    public YearMonthInputFields() {
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        List<Integer> selectableYears = IntStream.range(now.getYear() - 10, now.getYear() + 2).boxed().collect(Collectors.toList());

        yearPicker = new ComboBox<>("年", selectableYears);
        yearPicker.setValue(LocalDate.now().getYear());//set the default year
        yearPicker.setWidth(6, Unit.EM);
        monthPicker = new ComboBox<>("月", Month.values());
        monthPicker.setItemLabelGenerator(m -> m.getDisplayName(TextStyle.FULL, Locale.getDefault()));
        monthPicker.setWidth(9, Unit.EM);
        monthPicker.setValue(Month.of(LocalDate.now().getMonth().getValue()));//set the default month
        add(new HorizontalLayout(yearPicker, monthPicker));
    }

	public ComboBox<Integer> getYearPicker() {
		return yearPicker;
	}

	public ComboBox<Month> getMonthPicker() {
		return monthPicker;
	}
    
    public Integer getYear() {
    	return yearPicker.getValue();
    }
    /**
     * 返回大写英文月份：JANUARY...
     * @return
     */
    public Month getMonthLabel() {
    	return monthPicker.getValue();
    }
    
    public String getSelectedYearMonthFormat() {
    	Integer year = yearPicker.getValue();
    	Month value = monthPicker.getValue();
    	String displayName = value.getDisplayName(TextStyle.FULL, Locale.getDefault());
    	String monthNumber = Util.getMonthNumber(displayName);
    	return year + "-"+monthNumber;
    }
}
