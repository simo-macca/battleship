package project;

import static jtamaro.en.Graphics.*;
import static jtamaro.en.Sequences.*;

import jtamaro.en.Color;
import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import jtamaro.en.Sequences;
import jtamaro.en.Function1;
import jtamaro.en.Function2;

public final class Toolbelt {

    /**
   * Get the element at the given index.
   * Assumes the index is within the bounds of the sequence.
   * @param index the index (0-based position) of the element to get
   * @return the element at the given index
   */
    public static <T> T get(int index, Sequence<T> seq) {
        assert index >= 0;
        assert index < length(seq);
        int i = 0;
        Sequence<T> sequence = seq;
        while (true) {
            if (index == i) {
                return first(sequence);
            } 
            sequence = rest(sequence);
            i++;
        }
    }

    public static <T> int getIndex(T obj, Sequence<T> sequence) {
        Sequence<T> seq = sequence;
        int i = 0;
        while(!seq.isEmpty()) {
            if (obj.equals(first(seq))) {
                return i;
            }
            seq = rest(seq);
            i++;
        }
        return i = -1;
    }

    /**
     * Set the element at the given index to the given value.
     * Note that Sequence is immutable, so this method returns a new Sequence with
     * the element at the given index replaced.
     * Assumes the index is within the bounds of the sequence.
     * @param index the index (0-based position) of the element to set
     * @param value the new value for the element
     * @return a new Sequence with the element at the given index replaced
     */
    public static <T> Sequence<T> set(int index, T value, Sequence<T> seq) {
        assert index >= 0;
        assert index < length(seq);
        int i = 0;
        Sequence<T> newSeq = empty();
        Sequence<T> sequence = seq;
        while (i < length(seq)) {
            if (i != index) {
                newSeq = cons(first(sequence), newSeq);
            } else {
                newSeq = cons(value, newSeq);
            }
            sequence = rest(sequence);
            i++;
        }
        return reverse(newSeq);
    }

    /**
     * Remove the element from the sequence
     * @param obj the object to be deleted
     * @param sequence the list in which the object is present
     * @return the list without the object
     */
    public static <T> Sequence<T> remove(T obj, Sequence<T> sequence) {
        Sequence<T> seq = sequence;
        Sequence<T> result = empty();
        while (!seq.isEmpty()) {
            if (!obj.equals(seq.first())) {
                result = cons(seq.first(), result);
            }
            seq = seq.rest();
        }
        return reverse(result);
    }

    public static <T> Sequence<Sequence<T>> removeFromMatrix(T obj, Sequence<Sequence<T>> sequence) {
        Sequence<Sequence<T>> seq = sequence;
        Sequence<Sequence<T>> result = empty();
        while (!seq.isEmpty()) {
            Sequence<T> firstSeq = seq.first();
            Sequence<T> firstResult = empty();
            while (!firstSeq.isEmpty()) {
                if (!obj.equals(firstSeq.first())) {
                    firstResult = cons(firstSeq.first(), firstResult);
                }
                firstSeq = firstSeq.rest();
            }
            result = cons(reverse(firstResult), result);
            seq = seq.rest();
        }
        return reverse(result);
    }

    public static <T> int length(Sequence<T> seq) {
        return isEmpty(seq) ? 0 : 1 + length(rest(seq));
    }

    public static <T> boolean isInSequence(T obj, Sequence<T> seq){
        return isEmpty(seq)
        ? false
        : obj.equals(first(seq))
            ? true
                : isInSequence(obj, rest(seq));
    }

    public static <R,E> R reduce(R neutralElement, Function2<E,R,R> combiner, Sequence<E> sequence){
    return sequence.isEmpty()
        ? neutralElement
        : combiner.apply(Sequences.first(sequence), reduce(neutralElement, combiner, Sequences.rest(sequence)));
    }

    public static <A,B> Sequence<B> map(Function1<A,B> mapper, Sequence<A> sequence){
        return sequence.isEmpty()
            ? empty()
            : cons(mapper.apply(first(sequence)), map(mapper, rest(sequence)));
    }

    public static <E> Sequence<E> filter(Function1<E,Boolean> predicate, Sequence<E> sequence){
        return sequence.isEmpty()
            ? empty()
            : predicate.apply(first(sequence))
                ? cons(first(sequence), filter(predicate, rest(sequence)))
                : filter(predicate, rest(sequence));
    }

    // ------------------------------------ RENDERING ------------------------------------ //

    public static Graphic square(double sideLength, Color color) {
        return rectangle(sideLength, sideLength, color);
    }


    public static Graphic circle(double radius, Color color) {
        return circularSector(radius, 360, color);
    }

    public static Graphic composes(Sequence<Graphic> graphics) {
        return isEmpty(graphics)
            ? emptyGraphic()
            : compose(
                first(graphics),
                composes(rest(graphics))
            );
    }

    public static Graphic besides(Sequence<Graphic> graphics) {
        return isEmpty(graphics)
        ? emptyGraphic()
        : beside(
            first(graphics),
            besides(rest(graphics))
        );
    }

    public static Graphic aboves(Sequence<Graphic> graphics) {
        return isEmpty(graphics)
            ? emptyGraphic()
            : above(first(graphics), aboves(rest(graphics)));
    }

    public static Graphic overlays(Sequence<Graphic> graphics) {
        return isEmpty(graphics)
            ? emptyGraphic()
            : overlay(
                first(graphics),
                overlays(rest(graphics))
            );
    }
}