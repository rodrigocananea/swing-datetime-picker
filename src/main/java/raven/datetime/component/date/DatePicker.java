package raven.datetime.component.date;

import net.miginfocom.swing.MigLayout;
import raven.swing.slider.PanelSlider;
import raven.swing.slider.SimpleTransition;

import javax.swing.*;

public class DatePicker extends JPanel {

    private final DateSelection dateSelection = new DateSelection();
    private PanelMonth.EventMonthChanged eventMonthChanged;
    private PanelYear.EventYearChanged eventYearChanged;
    private int month = 10;
    private int year = 2023;

    /**
     * 0 as Date select
     * 1 as Month select
     * 2 as Year select
     */
    private int panelSelect = 0;

    public DatePicker() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,insets 10,fill", "[fill]"));
        panelSlider = new PanelSlider();
        header = new Header(getEventHeader());
        eventMonthChanged = createEventMonthChanged();
        eventYearChanged = createEventYearChanged();
        add(header);
        add(panelSlider, "width 260,height 250");
        panelSlider.addSlide(createPanelDate(10, 2023), null);
        add(new PanelDateOption(),"dock east,gap 0 10 10 10");
    }

    private Header.EventHeaderChanged getEventHeader() {
        return new Header.EventHeaderChanged() {

            @Override
            public void back() {
                setToBack();
            }

            @Override
            public void forward() {
                setToForward();
            }

            @Override
            public void monthSelected() {
                selectMonth();
            }

            @Override
            public void yearSelected() {
                selectYear();
            }
        };
    }

    private PanelMonth.EventMonthChanged createEventMonthChanged() {
        return new PanelMonth.EventMonthChanged() {
            @Override
            public void monthSelected(int month) {
                DatePicker.this.month = month;
                header.setDate(month, year);
                panelSlider.addSlide(createPanelDate(month, year), SimpleTransition.get(SimpleTransition.SliderType.DOWN_TOP));
                panelSelect = 0;
            }
        };
    }

    public PanelYear.EventYearChanged createEventYearChanged() {
        return new PanelYear.EventYearChanged() {
            @Override
            public void yearSelected(int year) {
                DatePicker.this.year = year;
                header.setDate(month, year);
                panelSlider.addSlide(createPanelMonth(month, year), SimpleTransition.get(SimpleTransition.SliderType.DOWN_TOP));
                panelSelect = 1;
            }
        };
    }

    public void setToBack() {
        if (panelSelect == 0) {
            if (month == 0) {
                month = 11;
                year--;
            } else {
                month--;
            }
            header.setDate(month, year);
            panelSlider.addSlide(createPanelDate(month, year), SimpleTransition.get(SimpleTransition.SliderType.BACK));
        } else if (panelSelect == 1) {
            year--;
            header.setDate(month, year);
            panelSlider.addSlide(createPanelMonth(month, year), SimpleTransition.get(SimpleTransition.SliderType.BACK));
        } else {
            PanelYear panelYear = (PanelYear) panelSlider.getComponent(1);
            panelSlider.addSlide(createPanelYear(panelYear.getYear() - PanelYear.YEAR_CELL), SimpleTransition.get(SimpleTransition.SliderType.BACK));
        }
    }

    public void setToForward() {
        if (panelSelect == 0) {
            if (month == 11) {
                month = 0;
                year++;
            } else {
                month++;
            }
            header.setDate(month, year);
            panelSlider.addSlide(createPanelDate(month, year), SimpleTransition.get(SimpleTransition.SliderType.FORWARD));
        } else if (panelSelect == 1) {
            year++;
            header.setDate(month, year);
            panelSlider.addSlide(createPanelMonth(month, year), SimpleTransition.get(SimpleTransition.SliderType.FORWARD));
        } else {
            PanelYear panelYear = (PanelYear) panelSlider.getComponent(1);
            panelSlider.addSlide(createPanelYear(panelYear.getYear() + PanelYear.YEAR_CELL), SimpleTransition.get(SimpleTransition.SliderType.FORWARD));
        }
    }

    public void selectMonth() {
        if (panelSelect != 1) {
            panelSlider.addSlide(createPanelMonth(month, year), SimpleTransition.get(panelSelect == 0 ? SimpleTransition.SliderType.TOP_DOWN : SimpleTransition.SliderType.DOWN_TOP));
            panelSelect = 1;
        } else {
            panelSlider.addSlide(createPanelDate(month, year), SimpleTransition.get(SimpleTransition.SliderType.DOWN_TOP));
            panelSelect = 0;
        }
    }

    public void selectYear() {
        if (panelSelect != 2) {
            panelSlider.addSlide(createPanelYear(year), SimpleTransition.get(SimpleTransition.SliderType.TOP_DOWN));
            panelSelect = 2;
        } else {
            panelSlider.addSlide(createPanelDate(month, year), SimpleTransition.get(SimpleTransition.SliderType.DOWN_TOP));
            panelSelect = 0;
        }
    }

    private PanelDate createPanelDate(int month, int year) {
        return new PanelDate(dateSelection, month, year);
    }

    private PanelMonth createPanelMonth(int month, int year) {
        return new PanelMonth(eventMonthChanged, dateSelection, month, year);
    }

    private PanelYear createPanelYear(int year) {
        return new PanelYear(eventYearChanged, dateSelection, year);
    }

    private Header header;
    private PanelSlider panelSlider;

    public DateSelectionMode getDateSelectionMode() {
        return dateSelection.dateSelectionMode;
    }

    public void setDateSelectionMode(DateSelectionMode dateSelectionMode) {
        this.dateSelection.dateSelectionMode = dateSelectionMode;
    }

    public enum DateSelectionMode {
        SINGLE_DATE_SELECTED, BETWEEN_DATE_SELECTED
    }
}
