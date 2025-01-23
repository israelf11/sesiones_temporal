package io.workshop.pruebas;

import io.temporal.activity.Activity;

import java.net.URL;
import java.util.Scanner;

public class ContentLengthActivityImpl implements ContentLengthActivity {
    @Override
    public ContentLengthInfo count(String url) {

        Activity.getExecutionContext().getTaskToken();
        System.out.println("Activity Type: " + Activity.getExecutionContext().getInfo().getActivityType());
        System.out.println("Called from workflow: " + Activity.getExecutionContext().getInfo().getWorkflowType());

        try {
            URL website = new URL(url);
            Scanner sc = new Scanner(website.openStream());
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext()) {
                sb.append(sc.next());
            }

            String result = sb.toString();
            result = result.replaceAll("<[^>]*>", "");

            ContentLengthInfo info = new ContentLengthInfo();
            info.add(url, result.length());
            return info;

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            throw Activity.wrap(e);
        }
    }
}
