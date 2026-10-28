package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        Assertions.assertTrue(timetable.getCountByCoaches().isEmpty());
    }

    @Test
    void testGetCountByCoachesCountsAllSessionsAndSortsDescending() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика", Age.CHILD, 60);
        Coach firstCoach = new Coach("Иванов", "Иван", "Иванович");
        Coach secondCoach = new Coach("Петров", "Пётр", "Петрович");
        Coach thirdCoach = new Coach("Сидоров", "Сидор", "Сидорович");

        timetable.addNewTrainingSession(new TrainingSession(group, firstCoach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, secondCoach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, secondCoach,
                DayOfWeek.THURSDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, secondCoach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, thirdCoach,
                DayOfWeek.TUESDAY, new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, thirdCoach,
                DayOfWeek.FRIDAY, new TimeOfDay(16, 0)));

        Map<Coach, Integer> counts = timetable.getCountByCoaches();

        Assertions.assertEquals(Map.of(firstCoach, 1, secondCoach, 3, thirdCoach, 2), counts);
        Assertions.assertEquals(List.of(secondCoach, thirdCoach, firstCoach),
                new ArrayList<>(counts.keySet()));
    }

    @Test
    void testGetCountByCoachesCombinesCoachesWithSameFullName() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика", Age.CHILD, 60);
        Coach firstCoach = new Coach("Иванов", "Иван", "Иванович");
        Coach secondCoach = new Coach("Иванов", "Иван", "Иванович");

        timetable.addNewTrainingSession(new TrainingSession(group, firstCoach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, secondCoach,
                DayOfWeek.THURSDAY, new TimeOfDay(14, 0)));

        Assertions.assertEquals(Map.of(firstCoach, 2), timetable.getCountByCoaches());
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        TreeMap<Integer, List<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertEquals(List.of(780), new ArrayList<>(mondaySessions.keySet()));
        Assertions.assertEquals(List.of(singleTrainingSession), mondaySessions.get(780));
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
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

        TreeMap<Integer, List<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        TreeMap<Integer, List<TrainingSession>> thursdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        Assertions.assertEquals(List.of(780), new ArrayList<>(mondaySessions.keySet()));
        Assertions.assertEquals(List.of(mondayChildTrainingSession), mondaySessions.get(780));
        Assertions.assertEquals(List.of(780, 1200), new ArrayList<>(thursdaySessions.keySet()));
        Assertions.assertEquals(List.of(thursdayChildTrainingSession), thursdaySessions.get(780));
        Assertions.assertEquals(List.of(thursdayAdultTrainingSession), thursdaySessions.get(1200));
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
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
            Assertions.assertTrue(timetable.getTrainingSessionsForDay(day).isEmpty());
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

        TreeMap<Integer, List<TrainingSession>> byDay =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> byTime = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 30));
        Assertions.assertEquals(List.of(810), new ArrayList<>(byDay.keySet()));
        Assertions.assertEquals(2, byDay.get(810).size());
        Assertions.assertTrue(byDay.get(810).containsAll(List.of(first, second)));
        Assertions.assertEquals(2, byTime.size());
        Assertions.assertTrue(byTime.containsAll(List.of(first, second)));
    } // вроде как переделал поля и ассерты под нынешнюю реализацию хранения данных

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

        TreeMap<Integer, List<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertEquals(List.of(785, 830), new ArrayList<>(mondaySessions.keySet()));
        Assertions.assertEquals(List.of(earlier), mondaySessions.get(785));
        Assertions.assertEquals(List.of(later), mondaySessions.get(830));
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
