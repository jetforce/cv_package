package cv_package.forms;

/**
 * Created by Jet on 04/03/2017.
 */

public abstract class Answer {

    public static int MARK_ANSWER = 2;
    public static int OPTICAL_ANSWER = 1;
    public static int BLOB_ANSWER =3;
    public int type;


    public Answer(int type){
        this.type = type;
    }
    public abstract Object getAnswer();
    public int getType(){
        return this.type;
    }

}
