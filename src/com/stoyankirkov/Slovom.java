package com.stoyankirkov;

public final class Slovom {

    /**
     * the entry point for using Slovom library spelling the number is partitioned.
     * Each part contains 1 to 3 digits. The last three contains also symbol for
     * decimal separator
     * @param amount expect number of type double which will be converted to words
     * @return a string containing amount in bulgarian
     */
    public static String toWords(double amount) {
        String billion = "милиард";
        String million = "милион";
        String thousand = "хиляди";
        String hundred = "";
        String fractions = "стотинки";
        StringBuilder sb = new StringBuilder();
        if (amount < 0) {
            sb.append("минус ");
            amount *= (-1);
        }
        String numStr = String.format("%.2f", amount);
        String[] nums = numStr.split("");
        int start = 0;

        if (nums.length > 15) {
            return "Сумата надхвърля 999 милиарда!";
        }
        if (nums.length > 12) {
            String[] tmp = getNumber(numStr, start);
            sb.append(addToWords(tmp, billion));
            start += tmp.length;
        }
        if (nums.length > 9) {
            String[] tmp = getNumber(numStr, start);
            sb.append(addToWords(tmp, million));
            start += tmp.length;
        }
        if (nums.length > 6) {
            String[] tmp = getNumber(numStr, start);
            sb.append(addToWords(tmp, thousand));
            start += tmp.length;
        }
        if (nums.length > 3) {
            String[] tmp = getNumber(numStr, start);
            sb.append(addToWords(tmp, hundred));
            start += tmp.length;
        }
        if (nums.length > 0) {
            String[] tmp = getNumber(numStr, start);
            sb.append(addToWords(tmp, fractions));
        }
        return sb.toString();
    }

    /**
     * method responsible for building the next part of spelling
     * @param nums digits of the number
     * @param count the part of number - billion, million, thousand, hundred, fractions
     * @return string containing bulgarian spelling for the number
     */
    private static String addToWords(String[] nums, String count) {
        StringBuilder sb = new StringBuilder();
        boolean isThousand = count.equals("хиляди");
        boolean isHundred = count.equals("");
        boolean fractions = count.equals("стотинки");
        boolean isZero = checkForZeros(nums);
        if (fractions) {
            sb.append(" и ");
            if (!nums[1].equals("0")) {
                sb.append(nums[1]);
            }
            sb.append(nums[2]).append(" ").append(count);
            return sb.toString();
        }
        if (isThousand && nums.length == 1) {
            if (nums[0].equals("1")) {
                sb.append("хиляда ");
            } else {
                sb.append(extractWords(nums, isThousand)).append(" ").append(count).append(" ");
            }
        } else if (!isZero) {
            sb.append(extractWords(nums, isThousand)).append(" ").append(count);
            if (!(nums.length == 1 && nums[0].equals("1")) && !isThousand && !isHundred) {
                sb.append("а ");
            } else {
                sb.append(" ");
            }
        }
        if (isHundred) {
            if (nums.length == 1 && nums[0].equals("0")) {
                sb.append("нула");
            }
            if (nums.length == 1 && nums[0].equals("1")) {
                return sb.toString().trim() + " лев";
            }
            return sb.toString().trim() + " лева";
        }
        return sb.toString();
    }

    /**
     * check if all digits are 0
     * @param nums digits
     * @return true if all digits are 0
     */
    private static boolean checkForZeros(String[] nums) {
        for (String num : nums) {
            if (!num.equals("0")) {
                return false;
            }
        }
        return true;
    }

    /**
     * check the number of digits that have to be extracted for spelling
     * @param str input string containing full number
     * @param start index from which the substring of the number will start
     * @return string array containing the next 3 digits of input number
     */
    private static String[] getNumber(String str, int start) {
        int lastDigits = 3;
        if (start == 0) {
            lastDigits = str.length() % 3;
            if (lastDigits == 0) {
                lastDigits = 3;
            }
        }
        String tmp = str.substring(start, (start + lastDigits));
        return tmp.split("");
    }

    /**
     * switch between methods depending on digits count
     * @param nums string array containing the digits of number
     * @param isThousand indicating if the number is part of thousands
     * @return string containing bulgarian spelling for the number
     */
    private static String extractWords(String[] nums, boolean isThousand) {
        switch (nums.length) {
            case 1:
                return numToWord(nums[0], isThousand);
            case 2:
                return tensToWords(nums, isThousand);
            case 3:
                return hundredsToWords(nums, isThousand);
            default:
                return "";
        }
    }

    /**
     * method spell hundreds to words using tensToWords method
     * @param nums string array containing the digits of number in range 0 - 999
     * @param isThousand indicating if the number is part of thousands
     * @return string containing bulgarian words for the number
     */
    private static String hundredsToWords(String[] nums, boolean isThousand) {
        String[] hundreds = new String[]{"", "сто", "двеста", "триста"};
        String hundred = "стотин";
        StringBuilder sb = new StringBuilder();

        switch (nums[0]) {
            case "0":
                return tensToWords(new String[]{nums[1], nums[2]}, isThousand);
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
                sb.append(numToWord(nums[0], isThousand)).append(hundred);
                break;
        }
        if (!(nums[1].equals("0") && nums[2].equals("0"))) {
            if (nums[1].equals("1") || nums[2].equals("0")) {
                sb.append(" и ");
            } else if (!nums[1].equals("0")) {
                sb.append(" ");
            }
            sb.append(tensToWords(new String[]{nums[1], nums[2]}, isThousand));
        }
        return sb.toString();
    }

    /**
     * method spell tens to words in bulgarian by fracturing the input
     * to numbers from 1 to 9 and using numToWord method
     * @param nums string array containing the digits of number in range 0 - 99
     * @param isThousand indicating if the number is part of thousands
     * @return string containing bulgarian words for the number
     */
    private static String tensToWords(String[] nums, boolean isThousand) {
        String ten = "десет";
        StringBuilder sb = new StringBuilder();

        if (nums[0].equals("0")) {
            sb.append(" и ").append(numToWord(nums[1], isThousand));
        } else if (nums[0].equals("1")) {
            if (nums[1].equals("0")) {
                return ten;
            }
            sb.append(numToWord(nums[1], isThousand = false));
            if (nums[1].equals("1")) {
                sb.append("a");
            } else {
                sb.append("на");
            }
            sb.append(ten);
        } else {
            sb.append(numToWord(nums[0], isThousand)).append(ten);
            if (!nums[1].equals("0")) {
                sb.append(" и ").append(numToWord(nums[1], isThousand));
            }
        }
        return sb.toString();
    }

    /**
     * method spell number from 1 to 9 in bulgarian
     * @param num input string containing number from 1 to 9
     * @param isThousand indicating if the number is part of thousands
     * @return string containing bulgarian word for the number
     */
    private static String numToWord(String num, boolean isThousand) {
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
                if (isThousand) {
                    sb.append("две");
                } else {
                    sb.append(numbers[2]);
                }
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
