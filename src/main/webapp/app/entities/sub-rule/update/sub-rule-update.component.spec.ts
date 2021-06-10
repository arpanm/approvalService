jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SubRuleService } from '../service/sub-rule.service';
import { ISubRule, SubRule } from '../sub-rule.model';
import { IApprovalRule } from 'app/entities/approval-rule/approval-rule.model';
import { ApprovalRuleService } from 'app/entities/approval-rule/service/approval-rule.service';

import { SubRuleUpdateComponent } from './sub-rule-update.component';

describe('Component Tests', () => {
  describe('SubRule Management Update Component', () => {
    let comp: SubRuleUpdateComponent;
    let fixture: ComponentFixture<SubRuleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let subRuleService: SubRuleService;
    let approvalRuleService: ApprovalRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SubRuleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SubRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubRuleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      subRuleService = TestBed.inject(SubRuleService);
      approvalRuleService = TestBed.inject(ApprovalRuleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApprovalRule query and add missing value', () => {
        const subRule: ISubRule = { id: 456 };
        const rule: IApprovalRule = { id: 11255 };
        subRule.rule = rule;

        const approvalRuleCollection: IApprovalRule[] = [{ id: 91233 }];
        spyOn(approvalRuleService, 'query').and.returnValue(of(new HttpResponse({ body: approvalRuleCollection })));
        const additionalApprovalRules = [rule];
        const expectedCollection: IApprovalRule[] = [...additionalApprovalRules, ...approvalRuleCollection];
        spyOn(approvalRuleService, 'addApprovalRuleToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ subRule });
        comp.ngOnInit();

        expect(approvalRuleService.query).toHaveBeenCalled();
        expect(approvalRuleService.addApprovalRuleToCollectionIfMissing).toHaveBeenCalledWith(
          approvalRuleCollection,
          ...additionalApprovalRules
        );
        expect(comp.approvalRulesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const subRule: ISubRule = { id: 456 };
        const rule: IApprovalRule = { id: 66715 };
        subRule.rule = rule;

        activatedRoute.data = of({ subRule });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(subRule));
        expect(comp.approvalRulesSharedCollection).toContain(rule);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const subRule = { id: 123 };
        spyOn(subRuleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ subRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subRule }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(subRuleService.update).toHaveBeenCalledWith(subRule);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const subRule = new SubRule();
        spyOn(subRuleService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ subRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subRule }));
        saveSubject.complete();

        // THEN
        expect(subRuleService.create).toHaveBeenCalledWith(subRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const subRule = { id: 123 };
        spyOn(subRuleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ subRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(subRuleService.update).toHaveBeenCalledWith(subRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApprovalRuleById', () => {
        it('Should return tracked ApprovalRule primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApprovalRuleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
