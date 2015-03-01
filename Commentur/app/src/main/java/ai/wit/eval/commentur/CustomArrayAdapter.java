package ai.wit.eval.commentur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rmvl on 3/1/2015.
 */

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> authors;
    private final List<String> comments;

    public CustomArrayAdapter(Context context, List<String> authors, List<String> comments) {
        super(context, R.layout.comments_screen, comments);
        this.context = context;
        this.authors = authors;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.comments_screen, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView dialogueView = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        nameView.setText(authors.get(position));
        dialogueView.setText(comments.get(position));

        return rowView;
    }


    public void addAll(List<String> addAuthors, List<String> addComments) {
        authors.addAll(addAuthors);
        comments.addAll(addComments);
    }

    public void add(String addAuthor, String addComment) {
        authors.add(addAuthor);
        comments.add(addComment);
    }

}
