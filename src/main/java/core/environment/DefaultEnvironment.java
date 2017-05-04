package core.environment;

import core.procedures.AFn;
import core.procedures.bit.BItShiftLeft;
import core.procedures.bit.BitAnd;
import core.procedures.bit.BitAndNot;
import core.procedures.bit.BitClear;
import core.procedures.bit.BitFlip;
import core.procedures.bit.BitNot;
import core.procedures.bit.BitOr;
import core.procedures.bit.BitSet;
import core.procedures.bit.BitShiftRight;
import core.procedures.bit.BitTest;
import core.procedures.bit.BitXor;
import core.procedures.characters.CharComparison;
import core.procedures.characters.CharPredicate;
import core.procedures.characters.CharProc;
import core.procedures.characters.IntegerToChar;
import core.procedures.cons.Append;
import core.procedures.cons.Car;
import core.procedures.cons.Cdr;
import core.procedures.cons.ConsProc;
import core.procedures.cons.ListRef;
import core.procedures.cons.ListTail;
import core.procedures.cons.SetCar;
import core.procedures.cons.SetCdr;
import core.procedures.delayed.Deliver;
import core.procedures.delayed.Deref;
import core.procedures.delayed.Force;
import core.procedures.delayed.FutureCancel;
import core.procedures.delayed.Promise;
import core.procedures.equivalence.Eq;
import core.procedures.equivalence.Equal;
import core.procedures.equivalence.Eqv;
import core.procedures.equivalence.Identical;
import core.procedures.exceptions.ExData;
import core.procedures.exceptions.ExInfo;
import core.procedures.functional.Apply;
import core.procedures.functional.ForEach;
import core.procedures.functional.MapProc;
import core.procedures.functional.Void;
import core.procedures.generic.AssocProc;
import core.procedures.generic.Conj;
import core.procedures.generic.Count;
import core.procedures.generic.Empty;
import core.procedures.generic.First;
import core.procedures.generic.Get;
import core.procedures.generic.Next;
import core.procedures.generic.Nth;
import core.procedures.generic.RandNth;
import core.procedures.generic.Range;
import core.procedures.generic.Reverse;
import core.procedures.generic.Sort;
import core.procedures.hashmaps.Find;
import core.procedures.hashmaps.HashMapProc;
import core.procedures.hashmaps.Key;
import core.procedures.hashmaps.Keys;
import core.procedures.hashmaps.Merge;
import core.procedures.hashmaps.Put;
import core.procedures.hashmaps.Val;
import core.procedures.hashmaps.Vals;
import core.procedures.hashmaps.Zipmap;
import core.procedures.interop.BigDecimalType;
import core.procedures.interop.BigIntegerType;
import core.procedures.interop.BooleanType;
import core.procedures.interop.CharType;
import core.procedures.interop.PrimitiveNumberType;
import core.procedures.io.CallWithInputFile;
import core.procedures.io.CallWithOutputFile;
import core.procedures.io.CloseInputPort;
import core.procedures.io.CloseOutputPort;
import core.procedures.io.ClosePort;
import core.procedures.io.CurrentInputPort;
import core.procedures.io.CurrentOutputPort;
import core.procedures.io.Display;
import core.procedures.io.IsCharReady;
import core.procedures.io.Load;
import core.procedures.io.Newline;
import core.procedures.io.OpenInputFile;
import core.procedures.io.OpenOutputFile;
import core.procedures.io.PeekChar;
import core.procedures.io.Println;
import core.procedures.io.Read;
import core.procedures.io.ReadChar;
import core.procedures.io.Write;
import core.procedures.io.WriteChar;
import core.procedures.keywords.Keyword;
import core.procedures.lists.ListProc;
import core.procedures.lists.MemberProc;
import core.procedures.math.Abs;
import core.procedures.math.Addition;
import core.procedures.math.Ceiling;
import core.procedures.math.Denominator;
import core.procedures.math.Division;
import core.procedures.math.Exp;
import core.procedures.math.Expt;
import core.procedures.math.Floor;
import core.procedures.math.GCD;
import core.procedures.math.LCM;
import core.procedures.math.Log;
import core.procedures.math.Max;
import core.procedures.math.Min;
import core.procedures.math.Modulo;
import core.procedures.math.Multiplication;
import core.procedures.math.Negation;
import core.procedures.math.Numerator;
import core.procedures.math.NumericalComparison;
import core.procedures.math.Quotient;
import core.procedures.math.Remainder;
import core.procedures.math.Round;
import core.procedures.math.Sqrt;
import core.procedures.math.Subtraction;
import core.procedures.math.ToExact;
import core.procedures.math.ToInexact;
import core.procedures.math.Truncate;
import core.procedures.math.complex.Angle;
import core.procedures.math.complex.ImagPart;
import core.procedures.math.complex.Magnitude;
import core.procedures.math.complex.MakePolar;
import core.procedures.math.complex.MakeRectangular;
import core.procedures.math.complex.RealPart;
import core.procedures.math.trigonometry.Acos;
import core.procedures.math.trigonometry.Asin;
import core.procedures.math.trigonometry.Atan;
import core.procedures.math.trigonometry.Cos;
import core.procedures.math.trigonometry.Cosh;
import core.procedures.math.trigonometry.Sin;
import core.procedures.math.trigonometry.Sinh;
import core.procedures.math.trigonometry.Tan;
import core.procedures.math.trigonometry.Tanh;
import core.procedures.meta.MetaProc;
import core.procedures.meta.WIthMeta;
import core.procedures.predicates.Predicate;
import core.procedures.sets.Difference;
import core.procedures.sets.Intersection;
import core.procedures.sets.IsSubset;
import core.procedures.sets.IsSuperset;
import core.procedures.sets.MapInvert;
import core.procedures.sets.SetProc;
import core.procedures.sets.Union;
import core.procedures.strings.EndsWith;
import core.procedures.strings.Includes;
import core.procedures.strings.IndexOf;
import core.procedures.strings.LastIndexOf;
import core.procedures.strings.ListToString;
import core.procedures.strings.Lowercase;
import core.procedures.strings.MakeString;
import core.procedures.strings.NumberToString;
import core.procedures.strings.StartsWith;
import core.procedures.strings.StringAppend;
import core.procedures.strings.StringComparison;
import core.procedures.strings.StringCopy;
import core.procedures.strings.StringFill;
import core.procedures.strings.StringLength;
import core.procedures.strings.StringProc;
import core.procedures.strings.StringRef;
import core.procedures.strings.StringSet;
import core.procedures.strings.StringToImmutableString;
import core.procedures.strings.StringToList;
import core.procedures.strings.StringToMutableString;
import core.procedures.strings.StringToNumber;
import core.procedures.strings.Substring;
import core.procedures.strings.Trim;
import core.procedures.strings.Uppercase;
import core.procedures.symbols.StringToSymbol;
import core.procedures.symbols.SymbolToString;
import core.procedures.system.Cast;
import core.procedures.system.ClassProc;
import core.procedures.system.ErrorProc;
import core.procedures.system.Eval;
import core.procedures.system.Exit;
import core.procedures.system.HashCode;
import core.procedures.system.Identity;
import core.procedures.system.IsInstance;
import core.procedures.system.Name;
import core.procedures.system.Num;
import core.procedures.system.Pst;
import core.procedures.system.RandomProc;
import core.procedures.system.Sleep;
import core.procedures.system.ToString;
import core.procedures.vectors.ListToVector;
import core.procedures.vectors.MakeVector;
import core.procedures.vectors.Vec;
import core.procedures.vectors.Vector;
import core.procedures.vectors.VectorFill;
import core.procedures.vectors.VectorImmutable;
import core.procedures.vectors.VectorLength;
import core.procedures.vectors.VectorRef;
import core.procedures.vectors.VectorSet;
import core.procedures.vectors.VectorToImmutableVector;
import core.procedures.vectors.VectorToList;
import core.scm.Symbol;
import core.scm.specialforms.And;
import core.scm.specialforms.Assert;
import core.scm.specialforms.Begin;
import core.scm.specialforms.CallCC;
import core.scm.specialforms.Case;
import core.scm.specialforms.Comment;
import core.scm.specialforms.Cond;
import core.scm.specialforms.Define;
import core.scm.specialforms.DefineSyntax;
import core.scm.specialforms.Delay;
import core.scm.specialforms.Do;
import core.scm.specialforms.Dot;
import core.scm.specialforms.DynamicWind;
import core.scm.specialforms.Else;
import core.scm.specialforms.Future;
import core.scm.specialforms.ISpecialForm;
import core.scm.specialforms.If;
import core.scm.specialforms.Lambda;
import core.scm.specialforms.Let;
import core.scm.specialforms.LetRec;
import core.scm.specialforms.LetRecSyntax;
import core.scm.specialforms.LetSeq;
import core.scm.specialforms.LetSyntax;
import core.scm.specialforms.New;
import core.scm.specialforms.Or;
import core.scm.specialforms.Quasiquote;
import core.scm.specialforms.Quote;
import core.scm.specialforms.Set;
import core.scm.specialforms.SyntaxRules;
import core.scm.specialforms.Throw;
import core.scm.specialforms.Time;
import core.scm.specialforms.Try;
import core.scm.specialforms.Unquote;
import core.scm.specialforms.UnquoteSplicing;

import java.util.ArrayList;
import java.util.List;

public final class DefaultEnvironment extends Environment {

  private static final AFn[] STANDARD_PROCEDURES = {
      /* Primitive Types */
      PrimitiveNumberType.BYTE,
      PrimitiveNumberType.SHORT,
      PrimitiveNumberType.INT,
      PrimitiveNumberType.LONG,
      PrimitiveNumberType.DOUBLE,
      PrimitiveNumberType.FLOAT,
      new BooleanType(),
      new CharType(),
      new BigIntegerType(),
      new BigDecimalType(),

      /* System */
      new Exit(),
      new IsInstance(),
      new Cast(),
      new ClassProc(),
      new ClassProc() { @Override public String getName() { return "class-of"; } },
      new ErrorProc(),
      new Pst(),
      new Eval(),
      new RandomProc(),
      new HashCode(),
      new HashCode() { @Override public String getName() { return "hash"; } },
      new ToString(),
      new ToString() { @Override public String getName() { return "str"; } },
      new Name(),
      new Identity(),
      new Num(),
      new Sleep(),

      /* Delayed */
      new Force(),
      new Promise(),
      new Deliver(),
      new Deref(),
      new FutureCancel(),

      /* Math */
      new Negation(),
      new Addition(),
      new Subtraction(),
      new Multiplication(),
      new Division(),
      new Abs(),
      new Sqrt(),
      new Expt(),
      new Exp(),
      new Log(),
      new Modulo(),
      new Modulo() { @Override public String getName() { return "mod"; } },
      new Remainder(),
      new Quotient(),
      new Quotient() { @Override public String getName() { return "quot"; } },
      new Round(),
      new Floor(),
      new Ceiling(),
      new Truncate(),
      new Max(),
      new Min(),
      new GCD(),
      new LCM(),
      new Numerator(),
      new Denominator(),
      new ToInexact(),
      new ToExact(),
      new RealPart(),
      new ImagPart(),
      new Magnitude(),
      new Angle(),
      new MakePolar(),
      new MakeRectangular(),

      /* Trigonometry */
      new Sin(),
      new Sinh(),
      new Cos(),
      new Cosh(),
      new Tan(),
      new Tanh(),
      new Asin(),
      new Acos(),
      new Atan(),

      /* Comparison & Equality */
      NumericalComparison.EQUAL,
      NumericalComparison.LESS,
      NumericalComparison.LESS_EQUAL,
      NumericalComparison.GREATER,
      NumericalComparison.GREATER_EQUAL,
      new Identical(),
      new Eq(),
      new Eqv(),
      new Equal(),

      /* Strings */
      new StringLength(),
      new StringCopy(),
      new StringProc(),
      new Substring(),
      new StringAppend(),
      new StringFill(),
      new MakeString(),
      new ListToString(),
      new NumberToString(),
      new StringToNumber(),
      new StringToList(),
      new StringRef(),
      new StringSet(),
      new StringToImmutableString(),
      new StringToMutableString(),
      StringComparison.STRING_GR,
      StringComparison.STRING_GR_CI,
      StringComparison.STRING_GR_OR_EQ,
      StringComparison.STRING_GR_OR_EQ_CI,
      StringComparison.STRING_LE,
      StringComparison.STRING_LE_CI,
      StringComparison.STRING_LE_OR_EQ,
      StringComparison.STRING_LE_OR_EQ_CI,
      StringComparison.STRING_EQ,
      StringComparison.STRING_EQ_CI,
      new EndsWith(),
      new Includes(),
      new IndexOf(),
      new LastIndexOf(),
      new Lowercase(),
      new StartsWith(),
      new Trim(),
      new Uppercase(),

      /* Characters */
      new IntegerToChar(),
      CharPredicate.IS_CHAR_WHITESPACE,
      CharPredicate.IS_CHAR_ALPHABETIC,
      CharPredicate.IS_CHAR_UPPER_CASE,
      CharPredicate.IS_CHAR_LOWER_CASE,
      CharPredicate.IS_CHAR_NUMERIC,
      CharProc.CHAR_TO_INTEGER,
      CharProc.CHAR_UPCASE,
      CharProc.CHAR_DOWNCASE,
      CharComparison.CHAR_GR,
      CharComparison.CHAR_GR_CI,
      CharComparison.CHAR_GR_OR_EQ,
      CharComparison.CHAR_GR_OR_EQ_CI,
      CharComparison.CHAR_LE,
      CharComparison.CHAR_LE_CI,
      CharComparison.CHAR_LE_OR_EQ,
      CharComparison.CHAR_LE_OR_EQ_CI,
      CharComparison.CHAR_EQ,
      CharComparison.CHAR_EQ_CI,

      /* IO */
      new Display(),
      new Display() { @Override public String getName() { return "print"; } },
      new Println(),
      new Newline(),
      new Load(),
      new Read(),
      new Write(),
      new ReadChar(),
      new PeekChar(),
      new WriteChar(),
      new IsCharReady(),
      new CurrentInputPort(),
      new CurrentOutputPort(),
      new ClosePort(),
      new CloseInputPort(),
      new CloseOutputPort(),
      new OpenInputFile(),
      new OpenOutputFile(),
      new CallWithInputFile(),
      new CallWithOutputFile(),

      /* Pairs */
      new MemberProc("member", new Equal()),
      new MemberProc("memq", new Eq()),
      new MemberProc("memv", new Eqv()),
      new AssocProc("assoc", new Equal()),
      new AssocProc("assq", new Eq()),
      new AssocProc("assv", new Eqv()),
      new ConsProc(),
      new Car(),
      new Cdr(),
      new SetCar(),
      new SetCdr(),
      new Append(),
      new Reverse(),
      new ListTail(),
      new ListRef(),
      new ListProc(),

      /* Symbols */
      new SymbolToString(),
      new StringToSymbol(),
      new StringToSymbol() { @Override public String getName() { return "symbol"; } },

      /* Vectors */
      new MakeVector(),
      new Vector(),
      new VectorImmutable(),
      new VectorLength(),
      new VectorRef(),
      new VectorSet(),
      new ListToVector(),
      new VectorToList(),
      new VectorFill(),
      new VectorToImmutableVector(),
      new Vec(),

      /* Functional */
      new Apply(),
      new MapProc(),
      new ForEach(),
      new Void(),

      /* Hashmaps */
      new Find(),
      new HashMapProc(),
      new Put(),
      new Key(),
      new Keys(),
      new Val(),
      new Vals(),
      new MapInvert(),
      new Merge(),
      new Zipmap(),

      /* Sets */
      new SetProc(),
      new Union(),
      new Intersection(),
      new Difference(),
      new IsSubset(),
      new IsSuperset(),

      /* Generic */
      new Count(),
      new Get(),
      new Nth(),
      new Sort(),
      new Count() { @Override public String getName() { return "length"; } },
      new Conj(),
      new Empty(),
      new Range(),
      new First(),
      new Next(),
      new Next() { @Override public String getName() { return "rest"; } },
      new RandNth(),

      /* Keywords */
      new Keyword(),

      /* Meta */
      new MetaProc(),
      new WIthMeta(),

      /* Exceptions */
      new ExData(),
      new ExInfo(),

      /* Bitwise */
      new BitAnd(),
      new BitAndNot(),
      new BitClear(),
      new BitFlip(),
      new BitNot(),
      new BitOr(),
      new BitSet(),
      new BItShiftLeft(),
      new BitShiftRight(),
      new BitTest(),
      new BitXor(),

      /* Predicates */
      Predicate.IS_NULL,
      Predicate.IS_NIL,
      Predicate.IS_EOF,
      Predicate.IS_SOME,
      Predicate.IS_PAIR,
      Predicate.IS_LIST,
      Predicate.IS_SET,
      Predicate.IS_MAP,
      Predicate.IS_MAP_ENTRY,
      Predicate.IS_COLL,
      Predicate.IS_PROMISE,
      Predicate.IS_FUTURE,
      Predicate.IS_FUTURE_DONE,
      Predicate.IS_FUTURE_CANCELLED,
      Predicate.IS_DELAY,
      Predicate.IS_REALIZED,
      Predicate.IS_CHAR,
      Predicate.IS_STRING,
      Predicate.IS_VECTOR,
      Predicate.IS_SYMBOL,
      Predicate.IS_BOOLEAN,
      Predicate.IS_TRUE,
      Predicate.IS_FALSE,
      Predicate.IS_PROC,
      Predicate.IS_PORT,
      Predicate.IS_INPUT_PORT,
      Predicate.IS_OUTPUT_PORT,
      Predicate.IS_NUMBER,
      Predicate.IS_COMPLEX,
      Predicate.IS_RATIONAL,
      Predicate.IS_RATIO,
      Predicate.IS_REAL,
      Predicate.IS_EXACT,
      Predicate.IS_INEXACT,
      Predicate.IS_ZERO,
      Predicate.IS_EVEN,
      Predicate.IS_ODD,
      Predicate.IS_EMPTY,
      Predicate.IS_INTEGER,
      Predicate.IS_POSITIVE,
      Predicate.IS_POS,
      Predicate.IS_NEGATIVE,
      Predicate.IS_NEG,
      Predicate.IS_IMMUTABLE,
      Predicate.IS_MUTABLE,
      Predicate.IS_KEYWORD,
      Predicate.IS_ANY,
      Predicate.IS_BLANK,
      Predicate.IS_CLASS,
      Predicate.IS_DECIMAL,
      Predicate.IS_FLOAT,
      Predicate.IS_FN,
      };

  private static final ISpecialForm[] SPECIAL_FORMS = new ISpecialForm[] {
    Delay.DELAY,
    Future.FUTURE,
    Quote.QUOTE,
    Set.SET,
    Quasiquote.QUASIQUOTE,
    Unquote.UNQUOTE,
    UnquoteSplicing.UNQUOTE_SPLICING,
    Time.TIME,
    Assert.ASSERT,
    /* With TCO */
    If.IF,
    Begin.BEGIN,
    And.AND,
    Or.OR,
    Lambda.LAMBDA,
    Define.DEFINE,
    Let.LET,
    LetRec.LETREC,
    LetSeq.LETSEQ,
    Do.DO,
    Case.CASE,
    Cond.COND,
    Else.ELSE,
    New.NEW,
    Comment.COMMENT,
    Dot.DOT,
    Throw.THROW,
    Try.TRY,
    DynamicWind.DYNAMIC_WIND,
    CallCC.CALL_WITH_CURRENT_CONTINUATION,
    CallCC.CALL_CC,
    // TODO Macros
    DefineSyntax.DEFINE_SYNTAX,
    LetSyntax.LET_SYNTAX,
    LetRecSyntax.LETREC_SYNTAX,
    SyntaxRules.SYNTAX_RULES
  };

  private static final List<String> LIBRARY_PROCEDURES = new ArrayList<>();
  static {
    /* Naive implementations (not via Continuations) */
    // TODO attach Metadata and mark these as pure?
    LIBRARY_PROCEDURES.add("(define values list)");
    LIBRARY_PROCEDURES.add("(define (call-with-values producer consumer) (apply consumer (producer)))");

    LIBRARY_PROCEDURES.add("(define (add1 n) (+ n 1))");
    LIBRARY_PROCEDURES.add("(define (inc  n) (+ n 1))");
    LIBRARY_PROCEDURES.add("(define (dec  n) (- n 1))");

    // TODO Implement in Java?
    LIBRARY_PROCEDURES.add(
      "(define rationalize" +
          "  (letrec ((check (lambda (x) (if (not (real? x))" +
          "                                (error (string-append \"Wrong argument type. Expected: Real, actual: \"" +
          "                                                      (->string x))))))" +
          "           (find-between " +
          "            (lambda (lo hi)" +
          "              (if (integer? lo)" +
          "                  lo" +
          "                (let ((lo-int (floor lo))" +
          "                      (hi-int (floor hi)))" +
          "                  (if (< lo-int hi-int)" +
          "                      (+ 1 lo-int)" +
          "                    (+ lo-int" +
          "                       (/ (find-between (/ (- hi lo-int)) (/ (- lo lo-int))))))))))" +
          "           (do-find-between" +
          "            (lambda (lo hi)" +
          "              (cond" +
          "               ((negative? lo) (- (find-between (- hi) (- lo))))" +
          "               (else (find-between lo hi))))))" +
          "    (lambda (x within)" +
          "      (check x) (check within)" +
          "      (let* ((delta (abs within))" +
          "             (lo (- x delta))" +
          "             (hi (+ x delta)))" +
          "        (cond" +
          "         ((equal? x +nan.0) x)" +
          "         ((or (equal? x +inf.0) " +
          "              (equal? x -inf.0))" +
          "          (if (equal? delta +inf.0) +nan.0 x))" +
          "         ((equal? delta +inf.0) 0.0)" +
          "         ((not (= x x)) +nan.0)" +
          "         ((<= lo 0 hi) (if (exact? x) 0 0.0))" +
          "         ((or (inexact? lo) (inexact? hi))" +
          "          (exact->inexact (do-find-between (inexact->exact lo) (inexact->exact hi))))" +
          "         (else (do-find-between lo hi)))))))");
  }

  public List<String> getLibraryProcedures() {
    return LIBRARY_PROCEDURES;
  }

  public DefaultEnvironment() {
    super(null);

    /* Special Forms */
    for (ISpecialForm specialForm : SPECIAL_FORMS) {
      put(Symbol.intern(specialForm.toString()), specialForm);
    }

    /* Standard Procedures */
    for (AFn proc : STANDARD_PROCEDURES) {
      put(Symbol.intern(proc.getName()), proc);
    }

    /* Constants and special cases, synonyms*/
    put(Symbol.intern("pi"), Math.PI);
    put(Symbol.intern("nil"), null);
    put(Symbol.intern("null"), null);
    put(Symbol.intern("eof"), null);
    put(Symbol.intern("call/cc"), get(Symbol.intern("call-with-current-continuation")));
    put(Symbol.intern("def"), get(Symbol.intern("define")));
    put(Symbol.intern("fn"), get(Symbol.intern("lambda")));
  }
}
