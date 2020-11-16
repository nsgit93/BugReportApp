package Client.Controller;

import Domain.IEmployee;
import Services.IObserver;
import Services.IService;

public interface ControlledScreen extends IObserver {
    public void setScreenParent(ScreenSwitcher screenPage);
    public void setService(IService service);
    public void setEmployee(IEmployee employee);
    public void loadTableData();
}
