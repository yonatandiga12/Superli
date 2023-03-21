package Globals.util;

import Globals.Enums.JobTitles;

import java.util.Comparator;

public class JobTitleComparator implements Comparator<JobTitles> {
    @Override
    public int compare(JobTitles o1, JobTitles o2) {
        return o1.ordinal() - o2.ordinal();
    }
}
