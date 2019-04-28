import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/** Ver2SpritePars.java
 *
 * Class for parsing a stage in Scratch Version 2
 * projects. This will parse through the project.json
 * from the submitted Scratch project and calculate a basic
 * count of comments, scripts, sounds, blocks, costumes, variables,
 * and lists used. Blocks are separated by category.
 *
 * @author Emily Macdonald
 */
public class Ver2SpritePars implements SpriteParser {

    private String[] variables;
    private String[] lists;

    private JSONObject sb2;
    private String name;
    private int scriptCount;
    private int variableCount;
    private int listCount;
    private int scriptCommentCount;
    private int soundCount;
    private int costumeCount;
    private int controlBlocksForSprite;
    private int dataBlocksForSprite;
    private int eventsBlocksForSprite;
    private int looksBlocksForSprite;
    private int moreBlocksBlocksForSprite;
    private int motionBlocksForSprite;
    private int operatorsBlocksForSprite;
    private int penBlocksForSprite;
    private int sensingBlocksForSprite;
    private int soundBlocksForSprite;

    /** constructor
    *
    * @param JSONObject sb2 - json file of the submitted Scratch project
    *                         (One Sprite)
    */
    public Sprite(JSONObject sb2) {

        this.sb2 = sb2;

        name =
            FileUtils.getJSONAttribute(jsonObj, "objName");
        scriptCount =
            FileUtils.getJSONArrayAttribute(jsonObj, "scripts").size();
        variableCount =
            FileUtils.getJSONArrayAttribute(jsonObj, "variables").size();
        listCount =
            FileUtils.getJSONArrayAttribute(jsonObj, "lists").size();
        scriptCommentCount =
            FileUtils.getJSONArrayAttribute(jsonObj, "scriptComments").size();
        soundCount =
            FileUtils.getJSONArrayAttribute(jsonObj, "sounds").size();
        costumeCount =
            FileUtils.getJSONArrayAttribute(jsonObj, "costumes").size();

        controlBlocksForSprite = 0;
        dataBlocksForSprite = 0;
        eventsBlocksForSprite = 0;
        looksBlocksForSprite = 0;
        moreBlocksBlocksForStage = 0;
        motionBlocksForStage = 0;
        operatorsBlocksForStage = 0;
        penBlocksForStage = 0;
        sensingBlocksForStage = 0;
        soundBlocksForStage = 0;

        JSONArray scripts =
            FileUtils.getJSONArrayAttribute(jsonObj, "scripts");
        processScripts(scripts);

        populateVariables();
        populateLists();
    }

    /** processScripts: void
     *
     * Process scripts to count blocks by category.
     *
     * @param JSONArray array - array of scripts for sprite
     * @return none
     */
    private void processScripts(JSONArray array) {

        // Base Case: scripts is empty
        if (array == null || array.size() == 0)
            return;

        // Action: If first element is a String, it is the block name.
        //         Get it and and count its category.
        if (array.get(0) instanceof String) {
            String category = Ver2ProjA.getCategory((String) array.get(0));

            if (category != null)
                switch (category) {
                    case "control":
                        controlBlocksForSprite++;
                        break;
                    case "data":
                        dataBlocksForSprite++;
                        break;
                    case "events":
                        eventsBlocksForSprite++;
                        break;
                    case "looks":
                        looksBlocksForSprite++;
                        break;
                    case "more blocks":
                        moreBlocksBlocksForSprite++;
                        break;
                    case "motion":
                        motionBlocksForSprite++;
                        break;
                    case "operators":
                        operatorsBlocksForSprite++;
                        break;
                    case "pen":
                        penBlocksForSprite++;
                        break;
                    case "sensing":
                        sensingBlocksForSprite++;
                        break;
                    case "sound":
                        soundBlocksForSprite++;
                        break;
                    default:
                        break;
                }
        }

        // Recursion: More elements in scripts that may
        //            represent embedded blocks.
        for (int i = 0; i < array.size(); i++)
            if (array.get(i) instanceof JSONArray)
                processScripts((JSONArray) array.get(i));

        return;
    }

    /** populateVariables: void
     *
     * Populate the array of variables.
     *
     * @param none
     * @return none
     */
    private void populateVariables() {

        JSONArray vars =
            FileUtils.getJSONArrayAttribute(jsonObj, "variables");
        variables = new String[vars.size()];
        JSONObject children = new JSONObject();

        for (int i = 0; i < vars.size(); i++) {
            children = (JSONObject) vars.get(i);
            variables[i] =
                FileUtils.getJSONAttribute(children, "name");;
        }
    }

    /** populateLists: void
     *
     * Populate the array of lists.
     *
     * @param none
     * @return none
     */
    private void populateLists() {

        JSONArray listArray =
            FileUtils.getJSONArrayAttribute(jsonObj, "lists");
        lists = new String[listArray.size()];
        JSONObject children = new JSONObject();

        for (int i = 0; i < listArray.size(); i++) {
            children = (JSONObject) listArray.get(i);
            lists[i] =
                FileUtils.getJSONAttribute(children, "listName");
        }
    }

    /** getVariableUsageCount: int
     *
     * (Sub-accessor)
     * Get variable usage count.
     *
     * @param String var - the variable being counted
     * @return int count - the number of times the variable is used
     */
    public int getVariableUsageCount(String var) {

        int count = 0;
        JSONArray scripts =
            FileUtils.getJSONArrayAttribute(jsonObj, "scripts");
        String spriteScript = scripts.toString();
        int pos = spriteScript.indexOf(var);

        while (pos >= 0) {
            pos += 1;
            count += 1;
            pos = spriteScript.indexOf(var, pos);
        }

        return count;
    }

    /** getListUsageCount: int
     *
     * (Sub-accessor)
     * Get list usage count.
     *
     * @param String list - the list being counted
     * @return int count - the number of times the list is used
     */
    public int getListUsageCount(String list) {

        int count = 0;
        JSONArray scripts =
            FileUtils.getJSONArrayAttribute(jsonObj, "scripts");
        String spriteScript = scripts.toString();
        int pos = spriteScript.indexOf(list);

        while (pos >= 0) {
            pos += 1;
            count += 1;
            pos = spriteScript.indexOf(list, pos);
        }

        return count;
    }

    //-----------------------------------------------------------
    //                      Field Accessors
    //-----------------------------------------------------------

    /** getName: String
     *
     * Get sprite name.
     *
     * @param none
     * @return String name - name of the sprite
     */
    public String getName() { return name; }

    /** getScriptCount: int
     *
     * Get script count for sprite.
     *
     * @param none
     * @return int scriptCount - number of scripts found
     */
    public int getScriptCount() { return scriptCount; }

    /** getVariableCount: int
     *
     * Get variable count for sprite.
     *
     * @param none
     * @return int variableCount - number of variables found
     */
    public int getVariableCount() { return variableCount; }

    /** getListCount: int
     *
     * Get list count for sprite.
     *
     * @param none
     * @return int listCount - number of lists found
     */
    public int getListCount() { return listCount; }

    /** getScriptCommentCount: int
     *
     * Get script comment count for sprite.
     *
     * @param none
     * @return int scriptCommentCount - number of script comments found
     */
    public int getScriptCommentCount() { return scriptCommentCount; }

    /** getSoundCount: int
     *
     * Get sound count for sprite.
     *
     * @param none
     * @return int soundCount - number of sounds found
     */
    public int getSoundCount() { return soundCount; }

    /** getCostumeCount: int
     *
     * Get costume count for sprite.
     *
     * @param none
     * @return int costumeCount - number of costumes found
     */
    public int getCostumeCount() { return costumeCount; }

    /** getControlBlocksForSprite: int
     *
     * Get control block count for sprite.
     *
     * @param none
     * @return int controlBlocksForSprite - number of control blocks found
     */
    public int getControlBlocksForSprite() { return controlBlocksForSprite; }

    /** getDataBlocksForSprite: int
     *
     * Get data block count for sprite.
     *
     * @param none
     * @return int dataBlocksForSprite - number of data blocks found
     */
    public int getDataBlocksForSprite() { return dataBlocksForSprite; }

    /** getEventsBlocksForSprite: int
     *
     * Get events block count for sprite.
     *
     * @param none
     * @return int eventsBlocksForSprite - number of events blocks found
     */
    public int getEventsBlocksForSprite() { return eventsBlocksForSprite; }

    /** getLooksBlocksForSprite: int
     *
     * Get looks block count for sprite.
     *
     * @param none
     * @return int looksBlocksForSprite - number of looks blocks found
     */
    public int getLooksBlocksForSprite() { return looksBlocksForSprite; }

    /** getMoreBlocksBlocksForSprite: int
     *
     * Get more blocks block count for sprite.
     *
     * @param none
     * @return int moreBlocksBlocksForSprite - number of "more blocks" blocks found
     */
    public int getMoreBlocksBlocksForSprite() { return moreBlocksBlocksForSprite; }

    /** getMotionBlocksForSprite: int
     *
     * Get motion block count for sprite.
     *
     * @param none
     * @return int motionBlocksForSprite - number of motion blocks found
     */
    public int getMotionBlocksForSprite() { return motionBlocksForSprite; }

    /** getOperatorsBlocksForSprite: int
     *
     * Get operators block count for sprite.
     *
     * @param none
     * @return int operatorsBlocksForSprite - number of operator blocks found
     */
    public int getOperatorsBlocksForSprite() { return operatorsBlocksForSprite; }

    /** getPenBlocksForSprite: int
     *
     * Get pen block count for sprite.
     *
     * @param none
     * @return int penBlocksForSprite - number of pen blocks found
     */
    public int getPenBlocksForSprite() { return penBlocksForSprite; }

    /** getSensingBlocksForSprite: int
     *
     * Get sensing block count for sprite.
     *
     * @param none
     * @return int sensingBlocksForSprite - number of sensing blocks found
     */
    public int getSensingBlocksForSprite() { return sensingBlocksForSprite; }

    /** getSoundBlocksForSprite: int
     *
     * Get sound block count for sprite.
     *
     * @param none
     * @return int soundBlocksForSprite - number of sound blocks found
     */
    public int getSoundBlocksForSprite() { return soundBlocksForSprite; }

    /** getVariables: String[]
     *
     * Get list of variables used.
     *
     * @param none
     * @return String[] variables - array of variables found
     */
    public String[] getVariables() { return variables; }

    /** getLists: String[]
     *
     * Get names of lists used.
     *
     * @param none
     * @return String[] lists - array of lists found
     */
    public String[] getLists() { return lists; }
}