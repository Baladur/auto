import IllegalArgumentError from '../util/errors/illegal-arg-error'
import Context from './context'

class Sequence {
    constructor() {
        this.i = -1;
        this.j = -1;
        this.links = [];
        this.sequences = new Array([]);
    }

    /**
     * Check that we are in sequence
     * @returns {boolean}
     */
    inSequence() {
        return this.i > -1 && this.j > -1;
    }

    /**
     * Get context object in sequence
     * @param index of object in sequence
     * @returns {*}
     */
    get(index) {
        return this.sequences[this.i][index];
    }

    /**
     * @returns {*} current context object in sequence
     */
    current() {
        return this.sequences[this.i][this.j];
    }

    /**
     * Push context object to the current sequence
     * @param obj
     */
    push(obj) {
        this.sequences[seq.i].push(obj);
    }

    /**
     * Get next object without moving cursor
     * @returns {*} context object next in sequence
     */
    next() {
        let temp_i = this.i;
        let temp_j = this.j + 1;
        let seek = temp_i;
        while (this.sequences[temp_i][temp_j] == Context.END) {
            if (temp_i == 0) {
                return Context.END;
            } else {
                const link = seq.links[seek--];
                temp_i = link.i;
                temp_j = link.j + 1;
            }
        }
        return this.sequences[temp_i][temp_j];
    }

    /**
     * Get previous context object without moving cursor
     */
    previous() {
        throw new Error("Not implemented");
    }

    /**
     * Move cursor forward and get context object
     * @returns {*} context object next in sequence
     */
    forward() {
        this.j++;
        let seek = this.i;
        while (this.current() == Context.END) {
            if (this.i == 0) {
                return Context.END;
            } else {
                const link = this.links[seek--];
                this.i = link.i;
                this.j = link.j + 1;
            }
        }
        return this.current();
    }

    /**
     * Move cursor backward and get context object
     * @returns {*}
     */
    backward() {
        while (true) {
            if (this.j - 1 < 0) {
                const link = this.links[this.i];
                this.i = link.i;
                this.j = link.j;
                break;
            }
            const backIndex = this.links.indexOf({
                i : this.i,
                j : this.j-1
            });
            if (backIndex >= 0) {
                this.i = backIndex;
                this.j = this.sequences[this.i].length-1;
            } else {
                this.j--;
                break;
            }
        }
        return this.current();
    }

    /**
     * Make sequence of given object
     * @param obj (must have 'seq' property or IllegalArgumentError will be thrown)
     */
    makeSequence(obj) {
		this.sequences.push([]);
        this.links.push({
            i : this.i,
            j : this.j
        });
        this.i = this.sequences.length - 1;
        this.j = 0;
        this._makeSequence(obj);
        this.push(Context.END);
    }

    /**
     * Recursive function called by makeSequence method
     * @param obj
     * @private
     */
    _makeSequence(obj) {
        if (!('seq' in obj))
            throw new IllegalArgumentError("_makeSequence", "Argument must have 'seq' property", obj)
        console.log("making sequence for ");
        console.log(obj);
        console.log("sequence: ");
        console.log(obj.seq);
        for (let i in obj.seq) {
            if ('seq' in obj.seq[i]) {
                _makeSequence(obj.seq[i]);
            } else {
                if (obj.seq[i] != Context.END) {
                    this.push(obj.seq[i]);
                }
            }
        }
    }
}

export default Sequence
