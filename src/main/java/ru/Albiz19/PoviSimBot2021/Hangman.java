package ru.Albiz19.PoviSimBot2021;

import java.util.ArrayList;
import java.util.List;

public class Hangman {
    private String cur_word_ = "";
    private String cur_word; //текущее слово, используемое в игре
    private List<Character> letters;
    private int mistakes = 0;
    private boolean isGameInProgress = true;
    private List<Character> usedLetters = new ArrayList<Character>();
    private Login login = new Login();
    Hangman(){
        chooseWord();
    }
    public List<Character> getLetters(){
        return letters;
    }
    public boolean isGameInProgress() {
        return isGameInProgress;
    }
    private boolean checkFinal(){
        if (letters.contains('_')){
            return false;
        }
        else {return true;}
    }
    private boolean checkGuess(char variant){
        boolean isguessed = false;
        int i, k=0;
        for (int index = 0; index<cur_word.length(); index++) {
            if ((i = cur_word.indexOf(variant, k)) != -1) {
                k = i+1;
                letters.set(i, variant);
                isguessed = true;
            }
        }
        return isguessed;
    }
    public String main(char msg){
        String message = letterGuess(msg);
        return message;
    }
    public void chooseWord(){
        //Метод должен выбрать слово из пула
        cur_word = login.getWord();
        System.out.print(cur_word);
        letters = new ArrayList<Character>();
        for (int i = 0; i<cur_word.length(); i++){
            letters.add('_');
        }
        System.out.print(cur_word);
        System.out.print(cur_word_);
    }
    public String letterGuess(char income_letter){
        if (checkGuess(income_letter)){
            if (checkFinal()){
                isGameInProgress = false;
                return "Ура! Ты угадал мое слово. Ответ: " + cur_word + ". \nВозврат " +
                        "в главное меню... \nВоспользуйтесь командами для взаимодействия с ботом.";
            }
            if (usedLetters.contains(income_letter)){
                return "Такая буква уже была!";
            }
            usedLetters.add(income_letter);
            return "Правильно! " + letters;
        }
        else {
            if (usedLetters.contains(income_letter)){
                return "Такая буква уже была!";
            }
            usedLetters.add(income_letter);
            mistakes++;
            if (mistakes==6){
                return "Попытки закончились! Игра окончена. Загаданным словом было слово " + cur_word + ".\nВозврат " +
                        "в главное меню... \nВоспользуйтесь командами для взаимодействия с ботом.";
            }
            return "Не угадал! Попробуй еще раз. Кол-во ошибок: " + mistakes + "/6. Слово: " + letters;
        }
    }

}
