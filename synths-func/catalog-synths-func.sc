(

ClioLibrary.catalog ([\func], {

	~synthArgs = ClioSynthFunc({ arg kwargs;

		var myKwargs = kwargs.reject {arg v,n; n==\synth;};

		myKwargs.pairsDo { arg name, value;
			kwargs[\synth][name] = value;
		};

	});

	// =====================================================================================

	~silenceFree = ClioSynthFunc({ arg kwargs;

		DetectSilence.ar( kwargs[\synth][\sig], time:kwargs[\time], doneAction:kwargs[\doneAction] );

	}, *[ // defaults:
		doneAction:2,
		time:0.1,
	]);

	// =====================================================================================

	~osc = ClioSynthFunc({ arg kwargs;

		kwargs[\synth][\sig] = kwargs[\oscType].ar(
			freq:kwargs[\synth][\freq],
			mul:kwargs[\ampMul],
			add:kwargs[\synth][\sig],
		);

	}, *[ // defaults:
		oscType:SinOsc,
		ampMul:1,
	]);

	// =====================================================================================

	~amp = ClioSynthFunc({ arg kwargs;
		kwargs[\synth][\sig] = kwargs[\synth][\sig] * (kwargs[\synth][\amp] ? 1) * kwargs[\ampMul];
	}, *[
		ampMul:1,
	]);

		// =====================================================================================

	~splay = ClioSynthFunc({ arg kwargs;
		kwargs[\synth][\sig] = Splay.ar(kwargs[\synth][\sig], kwargs[\spread]);
	}, *[
		spread:0.8
	]);

	~process = ClioSynthFunc({ arg kwargs;
		kwargs[\synth][\sig] = kwargs[\func].value(kwargs);

	}, *[ // set default kwargs:
		func:{},
	]);

	// TO DO... add AmpComp, possibly others


}
);

)
