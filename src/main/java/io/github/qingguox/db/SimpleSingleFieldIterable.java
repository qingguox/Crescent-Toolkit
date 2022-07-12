package io.github.qingguox.db;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.AbstractIterator;


/**
 * 功能: 获取单个数据源根据searchPosition开始count条数据.
 * 注意: source中需要I进行排序
 * @author wangqingwei
 * Created on 2022-06-22
 */
public class SimpleSingleFieldIterable<I extends Comparable<I>, R> implements Iterable<List<R>> {
    private final BiFunction<I, Integer, List<R>> searchSource;
    private I searchPosition;
    private final Integer singleCount;
    private final Function<R, I> model2PositionFunction;

    public SimpleSingleFieldIterable(I searchPosition,
            Integer singleCount, BiFunction<I, Integer, List<R>> searchSource, Function<R, I> model2PositionFunction) {
        this.searchSource = searchSource;
        this.searchPosition = searchPosition;
        this.singleCount = singleCount;
        this.model2PositionFunction = model2PositionFunction;
    }

    @Override
    public Iterator<List<R>> iterator() {
        return new SingleFieldIterator();
    }

    class SingleFieldIterator extends AbstractIterator<List<R>> {
        private boolean needContinued = true;

        @Override
        protected List<R> computeNext() {
            if (!needContinued) {
                return endOfData();
            }
            final List<R> searchResultList = searchSource.apply(searchPosition, singleCount);
            if (CollectionUtils.isEmpty(searchResultList)) {
                needContinued = false;
                return endOfData();
            }
            if (searchResultList.size() < singleCount) {
                needContinued = false;
            }
            searchPosition  = model2PositionFunction.apply(searchResultList.get(searchResultList.size() - 1));
            return searchResultList;
        }
    }
}
