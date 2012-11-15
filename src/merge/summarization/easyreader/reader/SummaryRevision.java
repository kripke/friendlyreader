
package merge.summarization.easyreader.reader;


/**
 *
 * @author christian
 */
public abstract class SummaryRevision {
    protected SummaryReader reader;
    public SummaryRevision(SummaryReader reader){
        this.reader = reader;
    }
}
