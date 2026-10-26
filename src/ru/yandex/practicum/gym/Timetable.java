package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, List<TrainingSession>> timetable = new LinkedHashMap<>(); // я решил его использовать тк он
    // сразу будет идти по расписанию и дает 0(1), тоесть добавлять по дням недели, как обычно и состовляют расписание
    private Map<DayOfWeek, TreeMap<Integer, List<TrainingSession>>> timetableForDayAndTime =
            new LinkedHashMap<>(); //решил продублировать чтобы получить О(log(n)) в getTrainingSessionsForDayAndTime

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        Comparator<TrainingSession> comparator = (s1, s2) -> {
            TimeOfDay timeOfDay1 = s1.getTimeOfDay();
            TimeOfDay timeOfDay2 = s2.getTimeOfDay();

            int minutes1 = timeOfDay1.getHours() * 60 + timeOfDay1.getMinutes();
            int minutes2 = timeOfDay2.getHours() * 60 + timeOfDay2.getMinutes();
            return Integer.compare(minutes1, minutes2);
        }; //тут я через компаратор сравниваю минуты чтобы понять, что стоит раньше по времени, а что позже


        if (timetable.containsKey(dayOfWeek)) {
            timetable.get(dayOfWeek).add(trainingSession);
        } else {
            List<TrainingSession> sessions = new ArrayList<>();
            sessions.add(trainingSession);
            timetable.put(dayOfWeek, sessions);
        }
        timetable.get(dayOfWeek).sort(comparator); //сортирую чтобы все расписание внутри было по времени

        TimeOfDay time = trainingSession.getTimeOfDay();
        int minutes = time.getHours() * 60 + time.getMinutes();

        if (!timetableForDayAndTime.containsKey(dayOfWeek)) {
            timetableForDayAndTime.put(dayOfWeek, new TreeMap<>());
        }

        TreeMap<Integer, List<TrainingSession>> daySessions =
                timetableForDayAndTime.get(dayOfWeek);

        if (!daySessions.containsKey(minutes)) {
            daySessions.put(minutes, new ArrayList<>());
        }

        daySessions.get(minutes).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.containsKey(dayOfWeek)) {
            return timetable.get(dayOfWeek);
        } else {
            return List.of(); // возвращаю пустрой список а не null
        }
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (!timetableForDayAndTime.containsKey(dayOfWeek)) {
            return List.of();
        }

        int minutes = timeOfDay.getHours() * 60 + timeOfDay.getMinutes();

        TreeMap<Integer, List<TrainingSession>> sessions =
                timetableForDayAndTime.get(dayOfWeek);

        return sessions.getOrDefault(minutes, List.of());
    }
}
