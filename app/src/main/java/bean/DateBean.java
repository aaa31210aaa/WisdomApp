package bean;


import com.bigkoo.pickerview.model.IPickerViewData;

public class DateBean implements IPickerViewData {
    private int id;
    private String year;

    public DateBean(int id, String year) {
        this.id = id;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String getPickerViewText() {
        return year;
    }
}
