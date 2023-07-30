package top.tangtian.datapump.common;

import java.util.Date;

public class MyThread extends Thread{

    public MyThread(Runnable target, String name) {
        super(target, name);
    }

    private Date creationDate;

    private Date startDate;

    private Date finishDate;


    public void run(){
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date date){
        this.startDate = date;
    }

    public void setStartDate() {
        this.setStartDate(new Date());
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
}
