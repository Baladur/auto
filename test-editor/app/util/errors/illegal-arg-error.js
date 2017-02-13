class IllegalArgumentError extends Error {
    constructor(functionName, expectedArgs, actualArgs) {
        const message = 'Illegal arguments passed to "' + functionName + '".\nExpected:\n' + expectedArgs + ';\nActual:\n' + actualArgs
        super(message)
    }
}

export default IllegalArgumentError