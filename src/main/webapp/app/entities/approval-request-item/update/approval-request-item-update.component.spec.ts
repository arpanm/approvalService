jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApprovalRequestItemService } from '../service/approval-request-item.service';
import { IApprovalRequestItem, ApprovalRequestItem } from '../approval-request-item.model';
import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { ApprovalRequestService } from 'app/entities/approval-request/service/approval-request.service';

import { ApprovalRequestItemUpdateComponent } from './approval-request-item-update.component';

describe('Component Tests', () => {
  describe('ApprovalRequestItem Management Update Component', () => {
    let comp: ApprovalRequestItemUpdateComponent;
    let fixture: ComponentFixture<ApprovalRequestItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let approvalRequestItemService: ApprovalRequestItemService;
    let approvalRequestService: ApprovalRequestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApprovalRequestItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApprovalRequestItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApprovalRequestItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      approvalRequestItemService = TestBed.inject(ApprovalRequestItemService);
      approvalRequestService = TestBed.inject(ApprovalRequestService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApprovalRequest query and add missing value', () => {
        const approvalRequestItem: IApprovalRequestItem = { id: 456 };
        const request: IApprovalRequest = { id: 26391 };
        approvalRequestItem.request = request;

        const approvalRequestCollection: IApprovalRequest[] = [{ id: 46320 }];
        spyOn(approvalRequestService, 'query').and.returnValue(of(new HttpResponse({ body: approvalRequestCollection })));
        const additionalApprovalRequests = [request];
        const expectedCollection: IApprovalRequest[] = [...additionalApprovalRequests, ...approvalRequestCollection];
        spyOn(approvalRequestService, 'addApprovalRequestToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ approvalRequestItem });
        comp.ngOnInit();

        expect(approvalRequestService.query).toHaveBeenCalled();
        expect(approvalRequestService.addApprovalRequestToCollectionIfMissing).toHaveBeenCalledWith(
          approvalRequestCollection,
          ...additionalApprovalRequests
        );
        expect(comp.approvalRequestsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const approvalRequestItem: IApprovalRequestItem = { id: 456 };
        const request: IApprovalRequest = { id: 56240 };
        approvalRequestItem.request = request;

        activatedRoute.data = of({ approvalRequestItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(approvalRequestItem));
        expect(comp.approvalRequestsSharedCollection).toContain(request);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalRequestItem = { id: 123 };
        spyOn(approvalRequestItemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalRequestItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approvalRequestItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(approvalRequestItemService.update).toHaveBeenCalledWith(approvalRequestItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalRequestItem = new ApprovalRequestItem();
        spyOn(approvalRequestItemService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalRequestItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approvalRequestItem }));
        saveSubject.complete();

        // THEN
        expect(approvalRequestItemService.create).toHaveBeenCalledWith(approvalRequestItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalRequestItem = { id: 123 };
        spyOn(approvalRequestItemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalRequestItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(approvalRequestItemService.update).toHaveBeenCalledWith(approvalRequestItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApprovalRequestById', () => {
        it('Should return tracked ApprovalRequest primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApprovalRequestById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
