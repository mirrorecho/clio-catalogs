(

ClioLibrary.catalog ([\func, \gen, \bass], {

	// TO DO... research the SOS UGen i.e. pyshical modeling of this more...
	// based on ixibass: see "https://github.com/codehearts/supercollider-music/blob/master/synths/ixibass.sc"
	// TO DO: this is interesting. How to develop? Is it used effectively?
	// ALSO: it doesn't perform very well
	~ixiBass = ClioSynthFunc({ arg kwargs, t_trig=1;

		var signal, signal1, signal2, b1, b2;

		b1 =  [0, 0.01, 0.02, 0.04] + 1.92; // 1.9522665452781; // = 1.98 * 0.989999999 * cos(0.09);
		b2 =  [0, 0.002, 0.004, 0.009] + 0.99 * -1; // -0.998057;
		// t_trig.scope;
		signal = K2A.ar(t_trig);
		// signal.scope;
		signal = SOS.ar(signal, 0.09, 0.0, 0.0, b1, b2, mul:kwargs[\synth][\amp]*0.12*kwargs[\ampMul]);
		signal = Splay.ar(signal, 0.66);
		signal = RHPF.ar(signal, kwargs[\synth][\freq], kwargs[\rq]) + RHPF.ar(signal, kwargs[\synth][\freq]/2, kwargs[\rq]);
		signal = Decay2.ar(signal, 0.1, 0.3) * signal; // TO DO ... research why this mul creates this effect...!

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + signal;

	}, *[
		rq: 0.008,
		ampMul:1
	]);


}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)
