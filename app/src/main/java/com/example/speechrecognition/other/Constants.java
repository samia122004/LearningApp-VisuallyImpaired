package com.example.speechrecognition.other;

public class Constants {


    // User-related constants
    public static final String USER = "user";
    public static final String SUBJECT = "subject";
    public static final String CORRECT = "correct";
    public static final String INCORRECT = "incorrect";
    public static final String QUESTIONS = "questions";
    public static final String totalQuestions = "totalQuestions";


    // Number of questions to show in each quiz
    public static final int QUESTION_SHOWING = 10;  // Update to the number of questions to show

    // Points for correct and incorrect answers
    public static final int CORRECT_POINT = 5;
    public static final int INCORRECT_POINT = 2;

    // Date format
    public static final String DATE_FORMAT = "dd MMM YYYY hh:mm a";

    // Firebase paths
    public static final String ENGLISH_QUESTIONS = "EnglishQuestions";  // Firebase path for English questions
    public static final String SCIENCE_QUESTIONS = "ScienceQuestions";  // Firebase path for Science questions
    public static final String MATH_QUESTIONS = "MathsQuestions";        // Firebase path for Math questions

    // Subjects constants
    public static final String ENGLISH = "English";  // Subject constant for English
    public static final String SCIENCE = "Science";  // Subject constant for Science
    public static final String MATH = "Math";        // Subject constant for Math

    // UI Labels and other string constants (useful if you need to dynamically set titles, etc.)
    public static final String LITERATURE_QUIZ = "English Quiz";  // Title for English quiz
    public static final String GEOGRAPHY_QUIZ = "Science Quiz";    // Title for Science quiz
    public static final String MATH_QUIZ = "Math Quiz";              // Title for Math quiz
}
