package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        Assertions.assertEquals(List.of(singleTrainingSession),
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY));
        Assertions.assertEquals(List.of(), timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        Assertions.assertEquals(List.of(mondayChildTrainingSession),
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY));
        Assertions.assertEquals(List.of(thursdayChildTrainingSession, thursdayAdultTrainingSession),
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY));
        Assertions.assertEquals(List.of(), timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        Assertions.assertEquals(List.of(singleTrainingSession),
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        Assertions.assertEquals(List.of(),
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)));
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        for (DayOfWeek day : DayOfWeek.values()) {
            Assertions.assertEquals(List.of(), timetable.getTrainingSessionsForDay(day));
            Assertions.assertEquals(List.of(),
                    timetable.getTrainingSessionsForDayAndTime(day, new TimeOfDay(13, 0)));
        }
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();
        Group children = new Group("Акробатика для детей", Age.CHILD, 60);
        Group adults = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach firstCoach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach secondCoach = new Coach("Иванов", "Пётр", "Алексеевич");
        TrainingSession first = new TrainingSession(children, firstCoach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 30));
        TrainingSession second = new TrainingSession(adults, secondCoach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 30));
        timetable.addNewTrainingSession(first);
        timetable.addNewTrainingSession(second);

        List<TrainingSession> byDay = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> byTime = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 30));
        Assertions.assertEquals(2, byDay.size());
        Assertions.assertTrue(byDay.containsAll(List.of(first, second)));
        Assertions.assertEquals(2, byTime.size());
        Assertions.assertTrue(byTime.containsAll(List.of(first, second)));
    }

    @Test
    void testSessionsAreSortedAndFilteredByDayAndMinutes() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession earlier = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 5));
        TrainingSession later = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 50));
        TrainingSession otherDay = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 5));
        timetable.addNewTrainingSession(later);
        timetable.addNewTrainingSession(otherDay);
        timetable.addNewTrainingSession(earlier);

        Assertions.assertEquals(List.of(earlier, later),
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY));
        Assertions.assertEquals(List.of(earlier), timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 5)));
        Assertions.assertEquals(List.of(later), timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 50)));
        Assertions.assertEquals(List.of(otherDay), timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.TUESDAY, new TimeOfDay(13, 5)));
        Assertions.assertEquals(List.of(), timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 6)));
        Assertions.assertEquals(List.of(), timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.SUNDAY, new TimeOfDay(13, 5)));
    }

}
