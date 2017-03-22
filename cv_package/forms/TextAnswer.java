package cv_package.forms;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class TextAnswer extends Answer {

    List<List<Mat>> answers;
    List<String> textResults;

    public TextAnswer(List<List<Mat>> answers){
        super(Answer.OPTICAL_ANSWER);
        this.answers = answers;
        textResults = new ArrayList<>();
    }


    @Override
    public Object getAnswer() {
        return this.answers;
    }
    @Override
    public Object getResult() {
        return this.textResults;
    }

    public void addResult(String text) {
        textResults.add(text);
    }
}
