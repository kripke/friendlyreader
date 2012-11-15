/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package merge.summarization.easyreader.util;

/**
 *
 * @author christian
 */
public class IndexHelper implements Comparable {

        private Float f;
        private int index;

        public IndexHelper(Float f, int index) {
            this.f = f;
            this.index = index;
        }

        @Override
        public String toString() {
            return new StringBuilder().append("IndexHelper_index: ").
                    append(index).
                    append("_value: ").
                    append(f.toString()).toString();
        }

        public int getIndex() {
            return index;
        }

        public Float getValue(){
            return f;
        }

        public int compareTo(Object o) {
            IndexHelper b = (IndexHelper) o;
            if (b.f < f) {
                return -1;
            }
            if (b.f > f) {
                return 1;
            } else {
                return 0;
            }
        }
    }