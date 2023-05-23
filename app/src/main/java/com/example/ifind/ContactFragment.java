package com.example.ifind;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {
    EditText subject, inquiry;

    Button submit_inquiry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submit_inquiry = view.findViewById(R.id.submit_inquiry);
        EditText subject = view.findViewById(R.id.subject_layout);
        EditText inquiry = view.findViewById(R.id.inquiry);

        submit_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectText = subject.getText().toString();
                String subjectBody =inquiry.getText().toString();
                sendEmail(subjectText, subjectBody);

            }
        });
    }

    private void sendEmail(String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ramfind04@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            requireActivity().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No email app found
            Toast.makeText(requireContext(), "No email app found", Toast.LENGTH_SHORT).show();
            Log.e("ContactFragment", "No email app found", e);
        } catch (Exception e) {

            Toast.makeText(requireContext(), "Error sending email", Toast.LENGTH_SHORT).show();
            Log.e("ContactFragment", "Error sending email", e);
        }
    }
}