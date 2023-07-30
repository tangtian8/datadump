package top.tangtian.datapump.constants;

public enum OptType {
    New("New"),
    Update("Update"),
    Delete("Delete");

    private String actionName;

    private OptType(String actionName){
        this.actionName = actionName;
    }

    public String getActionName(){
        return this.actionName;
    }

    public final static OptType getActionByName(String actionName){
        if (New.getActionName().equals(actionName)){
            return New;
        }else if (Update.getActionName().equals(actionName)){
            return Update;
        }else {
            return Delete.getActionName().equals(actionName) ? Delete : null;
        }
    }
}
