package com.stoyankirkov;

public class Main {

    private static boolean isThousand = false;

    public static void main(String[] args) {
        double amount = 101101101123.999;
        toWords(amount);
    }

    private static void toWords(double amount) {
        String billion = "милиард";
        String million = "милион";
        String thousand = "хиляди";
        String numStr = String.format("%.2f", amount);
        String[] nums = numStr.split("");
        System.out.println(extractWords(new String[]{"3", "5"}));
        StringBuilder sb = new StringBuilder();

        if (nums.length > 15) {
            System.out.println("Сумата надхвърля 999 милиарда!");
            return;
        }
        if (nums.length > 12) {
            String[] tmp = getNumber(numStr, 0);
            sb.append(addToWords(tmp, billion));
        }
        if (nums.length > 9) {
            String[] tmp = getNumber(numStr, 3);
            sb.append(addToWords(tmp, million));
        }
        if (nums.length > 6) {
            isThousand = true;
            String[] tmp = getNumber(numStr, 6);
            sb.append(addToWords(tmp, thousand));
            isThousand = false;
        }
        System.out.println(sb.toString());
    }

    private static String addToWords(String[] nums, String count) {
        StringBuilder sb = new StringBuilder();
        sb.append(extractWords(nums)).append(" ").append(count);
        if (!(nums.length == 1 && nums[0].equals("1")) && !count.equals("хиляди")) {
            sb.append("а ");
        }
        return sb.toString();
    }

    private static String[] getNumber(String str, int start) {
        int lastDigits = 3;
        if (start == 0) {
            lastDigits = 3 - (str.length() % 3);
        }
        String tmp = str.substring(start, (start + lastDigits));
        return tmp.split("");
    }

    private static String extractWords(String[] nums) {
        switch (nums.length) {
            case 1:
                return numToWord(nums[0]);
            case 2:
                return tensToWords(nums);
            case 3:
                return hundredsToWords(nums);
            default:
                return "";
        }
    }

    private static String hundredsToWords(String[] nums) {
        String[] hundreds = new String[]{"", "сто", "двеста", "триста"};
        String hundred = "стотин";
        StringBuilder sb = new StringBuilder();

        switch (nums[0]) {
            case "1":
                sb.append(hundreds[1]);
                break;
            case "2":
                sb.append(hundreds[2]);
                break;
            case "3":
                sb.append(hundreds[3]);
                break;
            default:
                sb.append(numToWord(nums[0])).append(hundred);
                break;
        }
        if (!(nums[1].equals("0") && nums[2].equals("0"))) {
            if (nums[1].equals("1") || nums[2].equals("0")) {
                sb.append(" и ");
            } else if (!nums[1].equals("0")){
                sb.append(" ");
            }
            sb.append(tensToWords(new String[]{nums[1], nums[2]}));
        }
        return sb.toString();
    }

    private static String tensToWords(String[] nums) {
        String ten = "десет";
        StringBuilder sb = new StringBuilder();

        if (nums[0].equals("0")) {
            sb.append(" и ").append(numToWord(nums[1]));
        } else if (nums[0].equals("1")) {
            if (nums[1].equals("0")) {
                return ten;
            }
            sb.append(numToWord(nums[1]));
            if (nums[1].equals("1")) {
                sb.append("a");
            } else {
                sb.append("на");
            }
            sb.append(ten);
        } else {
            sb.append(numToWord(nums[0])).append(ten);
            if (!nums[1].equals("0")) {
                sb.append(" и ").append(numToWord(nums[1]));
            }
        }
        return sb.toString();
    }

    private static String numToWord(String num) {
        String[] numbers = new String[]{"", "един", "два", "три", "четири", "пет", "шест", "седем", "осем", "девет"};
        StringBuilder sb = new StringBuilder();

        switch (num) {
            case "1":
                if (isThousand) {
                    sb.append("една");
                } else {
                    sb.append(numbers[1]);
                }
                break;
            case "2":
                sb.append(numbers[2]);
                break;
            case "3":
                sb.append(numbers[3]);
                break;
            case "4":
                sb.append(numbers[4]);
                break;
            case "5":
                sb.append(numbers[5]);
                break;
            case "6":
                sb.append(numbers[6]);
                break;
            case "7":
                sb.append(numbers[7]);
                break;
            case "8":
                sb.append(numbers[8]);
                break;
            case "9":
                sb.append(numbers[9]);
                break;
            default:
                break;
        }
        return sb.toString();
    }
}
