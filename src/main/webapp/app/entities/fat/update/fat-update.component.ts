import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFat, Fat } from '../fat.model';
import { FatService } from '../service/fat.service';

@Component({
  selector: 'jhi-fat-update',
  templateUrl: './fat-update.component.html',
})
export class FatUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    inci: [],
    sapNaoh: [],
    sapKoh: [],
    lauric: [],
    myristic: [],
    palmitic: [],
    stearic: [],
    ricinoleic: [],
    oleic: [],
    linoleic: [],
    linolenic: [],
    iodine: [],
    ins: [],
  });

  constructor(protected fatService: FatService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fat }) => {
      this.updateForm(fat);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fat = this.createFromForm();
    if (fat.id !== undefined) {
      this.subscribeToSaveResponse(this.fatService.update(fat));
    } else {
      this.subscribeToSaveResponse(this.fatService.create(fat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fat: IFat): void {
    this.editForm.patchValue({
      id: fat.id,
      name: fat.name,
      inci: fat.inci,
      sapNaoh: fat.sapNaoh,
      sapKoh: fat.sapKoh,
      lauric: fat.lauric,
      myristic: fat.myristic,
      palmitic: fat.palmitic,
      stearic: fat.stearic,
      ricinoleic: fat.ricinoleic,
      oleic: fat.oleic,
      linoleic: fat.linoleic,
      linolenic: fat.linolenic,
      iodine: fat.iodine,
      ins: fat.ins,
    });
  }

  protected createFromForm(): IFat {
    return {
      ...new Fat(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      inci: this.editForm.get(['inci'])!.value,
      sapNaoh: this.editForm.get(['sapNaoh'])!.value,
      sapKoh: this.editForm.get(['sapKoh'])!.value,
      lauric: this.editForm.get(['lauric'])!.value,
      myristic: this.editForm.get(['myristic'])!.value,
      palmitic: this.editForm.get(['palmitic'])!.value,
      stearic: this.editForm.get(['stearic'])!.value,
      ricinoleic: this.editForm.get(['ricinoleic'])!.value,
      oleic: this.editForm.get(['oleic'])!.value,
      linoleic: this.editForm.get(['linoleic'])!.value,
      linolenic: this.editForm.get(['linolenic'])!.value,
      iodine: this.editForm.get(['iodine'])!.value,
      ins: this.editForm.get(['ins'])!.value,
    };
  }
}
