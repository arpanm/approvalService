jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SubRuleInListItemService } from '../service/sub-rule-in-list-item.service';
import { ISubRuleInListItem, SubRuleInListItem } from '../sub-rule-in-list-item.model';
import { ISubRule } from 'app/entities/sub-rule/sub-rule.model';
import { SubRuleService } from 'app/entities/sub-rule/service/sub-rule.service';

import { SubRuleInListItemUpdateComponent } from './sub-rule-in-list-item-update.component';

describe('Component Tests', () => {
  describe('SubRuleInListItem Management Update Component', () => {
    let comp: SubRuleInListItemUpdateComponent;
    let fixture: ComponentFixture<SubRuleInListItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let subRuleInListItemService: SubRuleInListItemService;
    let subRuleService: SubRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SubRuleInListItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SubRuleInListItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubRuleInListItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      subRuleInListItemService = TestBed.inject(SubRuleInListItemService);
      subRuleService = TestBed.inject(SubRuleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SubRule query and add missing value', () => {
        const subRuleInListItem: ISubRuleInListItem = { id: 456 };
        const subRule: ISubRule = { id: 25045 };
        subRuleInListItem.subRule = subRule;

        const subRuleCollection: ISubRule[] = [{ id: 1434 }];
        spyOn(subRuleService, 'query').and.returnValue(of(new HttpResponse({ body: subRuleCollection })));
        const additionalSubRules = [subRule];
        const expectedCollection: ISubRule[] = [...additionalSubRules, ...subRuleCollection];
        spyOn(subRuleService, 'addSubRuleToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ subRuleInListItem });
        comp.ngOnInit();

        expect(subRuleService.query).toHaveBeenCalled();
        expect(subRuleService.addSubRuleToCollectionIfMissing).toHaveBeenCalledWith(subRuleCollection, ...additionalSubRules);
        expect(comp.subRulesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const subRuleInListItem: ISubRuleInListItem = { id: 456 };
        const subRule: ISubRule = { id: 87911 };
        subRuleInListItem.subRule = subRule;

        activatedRoute.data = of({ subRuleInListItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(subRuleInListItem));
        expect(comp.subRulesSharedCollection).toContain(subRule);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const subRuleInListItem = { id: 123 };
        spyOn(subRuleInListItemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ subRuleInListItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subRuleInListItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(subRuleInListItemService.update).toHaveBeenCalledWith(subRuleInListItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const subRuleInListItem = new SubRuleInListItem();
        spyOn(subRuleInListItemService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ subRuleInListItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subRuleInListItem }));
        saveSubject.complete();

        // THEN
        expect(subRuleInListItemService.create).toHaveBeenCalledWith(subRuleInListItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const subRuleInListItem = { id: 123 };
        spyOn(subRuleInListItemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ subRuleInListItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(subRuleInListItemService.update).toHaveBeenCalledWith(subRuleInListItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSubRuleById', () => {
        it('Should return tracked SubRule primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSubRuleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
