public class Options {
    public String[] options;
    public Options(String[] args) {
        options = new String[11];
        for (int i = 0; i < args.length; i++) {
            if(args[i].charAt(0) == '-') {
                switch (args[i].charAt(1)) {
                    case 'f': options[0] = args[i+1];
                    break;
                    case 'a': options[1] = args[i+1];
                    break;
                    case 'e': options[2] = args[i+1];
                    break;
                    case 'g': options[3] = args[i+1];
                    break;
                    case 'n': if(args[i].charAt(2) == 'a') options[4] = args[i+1];
                    else options[5] = args[i+1];
                    break;
                    case 'p': options[6] = args[i+1];
                    break;
                    case 'q': options[7] = "true";
                    break;
                    case 'T': options[8] = args[i+1];
                    break;
                    case 'u': options[9] = "true";
                    break;
                    case 'v': options[10] = args[i+1];
                    break;
                    case 'P':options[11] = "true";
                }
            }
        }
    }
}