(

ClioLibrary.catalog ([\func, \env], {


	~perc = { arg kwargs;
		var doneAction = kwargs[\def_perc_doneAction] ? 2;

		var env = EnvGen.kr(Env.perc(
			attackTime: \attackTime.ir( kwargs[\attackTime] ? 0.01 ),
			releaseTime: \releaseTime.ir( kwargs[\releaseTime] ? 2 ),
			level:kwargs[\amp],
			curve: \curve.ir( kwargs[\curve] ? -4 ),
		), doneAction:doneAction);

		kwargs[\sig] = kwargs[\sig] * env;
	};


	~asr = { arg kwargs;
		var doneAction = kwargs[\def_asr_doneAction] ? 2;

		var env = EnvGen.kr(Env.asr(
			attackTime: \attackTime.ir( kwargs[\attackTime] ? 0.01 ),
			sustainLevel:kwargs[\amp],
			releaseTime: \releaseTime.ir( kwargs[\releaseTime] ? 2 ),
			curve: \curve.ir( kwargs[\curve] ? -4 ),
		), gate:kwargs[\gate], doneAction:doneAction);

		kwargs[\sig] = kwargs[\sig] * env;
	};


	~adsr = { arg kwargs;
		var doneAction = kwargs[\def_adsr_doneAction] ? 2;

		var env = EnvGen.kr(Env.adsr(
			attackTime: \attackTime.ir( kwargs[\attackTime] ? 0.01 ),
			decayTime: \decayTime.ir( kwargs[\decayTime] ? 0.3 ),
			sustainLevel: \sustainLevel.ir( kwargs[\sustainLevel] ? 0.69 ),
			releaseTime: \releaseTime.ir( kwargs[\releaseTime] ? 2 ),
			peakLevel: kwargs[\amp],
			curve: \curve.ir( kwargs[\curve] ? -4 ),
		), gate:kwargs[\gate], doneAction:doneAction);

		kwargs[\sig] = kwargs[\sig] * env;
	};


	~swell = { arg kwargs;
		var doneAction = kwargs[\def_swell_doneAction] ? 2;

		var env = EnvGen.kr(Env.perc(
			attackTime: \attackTime.ir( kwargs[\attackTime] ? 2 ),
			releaseTime: \releaseTime.ir( kwargs[\releaseTime] ? 0.01 ),
			level:kwargs[\amp],
			curve: \curve.ir( kwargs[\curve] ? 2 ),
		), doneAction:doneAction);

		kwargs[\sig] = kwargs[\sig] * env;
	};

	~silenceFree = { arg kwargs;
		var doneAction = kwargs[\def_silenceFree_doneAction] ? 2;

		DetectSilence.ar( kwargs[\sig], doneAction: 2 );
	};

	~ampScale = { arg kwargs;
		kwargs[\sig] = kwargs[\sig] * kwargs[\amp];
	};


});


)
